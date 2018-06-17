package pt.tumba.ngram.svm;

/** Generalized SMO+SVMlight algorithm
 * Solves:
 *
 *min 0.5(\alpha^T Q \alpha) + b^T \alpha
 *
 *	y^T \alpha = \delta
 *  y_i = +1 or -1
*		0 <= alpha_i <= Cp for y_i = 1
*		0 <= alpha_i <= Cn for y_i = -1
*
* Given:
*
*	Q, b, y, Cp, Cn, and an initial feasible point \alpha
*	l is the size of vectors and matrices
*	eps is the stopping criterion
*
* solution will be put in \alpha, objective value will be put in obj
*/
public class Solver {

	int activeSize;
	byte[] y;
	double[] G;		// gradient of objective function
	static final byte LOWER_BOUND = 0;
	static final byte UPPER_BOUND = 1;
	static final byte FREE = 2;
	byte[] alphaStatus;	// LOWER_BOUND, UPPER_BOUND, FREE
	double[] alpha;
	Kernel Q;
	double eps;
	double Cp,Cn;
	double[] b;
	int[] activeSet;
	double[] GBar;		// gradient, if we treat free variables as 0
	int l;
	boolean unshrinked;	// XXX
	
	static final double INF = java.lang.Double.POSITIVE_INFINITY;

	double getC(int i)
	{
		return (y[i] > 0)? Cp : Cn;
	}
	void updateAlphaStatus(int i)
	{
		if(alpha[i] >= getC(i))
			alphaStatus[i] = UPPER_BOUND;
		else if(alpha[i] <= 0)
			alphaStatus[i] = LOWER_BOUND;
		else alphaStatus[i] = FREE;
	}
	boolean isUpperBound(int i) { return alphaStatus[i] == UPPER_BOUND; }
	boolean isLowerBound(int i) { return alphaStatus[i] == LOWER_BOUND; }
	boolean isFree(int i) {  return alphaStatus[i] == FREE; }

	// java: information about solution except alpha,
	// because we cannot return multiple values otherwise...
	static class SolutionInfo {
		double obj;
		double rho;
		double upper_bound_p;
		double upper_bound_n;
		double r;	// for Solver_NU
	}

	void swapIndex(int i, int j)
	{
		Q.swapIndex(i,j);
		do {byte _=y[i]; y[i]=y[j]; y[j]=_;} while(false);
		do {double _=G[i]; G[i]=G[j]; G[j]=_;} while(false);
		do {byte _=alphaStatus[i]; alphaStatus[i]=alphaStatus[j]; alphaStatus[j]=_;} while(false);
		do {double _=alpha[i]; alpha[i]=alpha[j]; alpha[j]=_;} while(false);
		do {double _=b[i]; b[i]=b[j]; b[j]=_;} while(false);
		do {int _=activeSet[i]; activeSet[i]=activeSet[j]; activeSet[j]=_;} while(false);
		do {double _=GBar[i]; GBar[i]=GBar[j]; GBar[j]=_;} while(false);
	}

	void reconstructGradient()
	{
		// reconstruct inactive elements of G from G_bar and free variables

		if(activeSize == l) return;

		int i;
		for(i=activeSize;i<l;i++)
			G[i] = GBar[i] + b[i];

		for(i=0;i<activeSize;i++)
			if(isFree(i))
			{
				float[] Q_i = Q.getQ(i,l);
				double alpha_i = alpha[i];
				for(int j=activeSize;j<l;j++)
					G[j] += alpha_i * Q_i[j];
			}
	}

	void Solve(int l, Kernel Q, double[] b_, byte[] y_,
		   double[] alpha_, double Cp, double Cn, double eps, SolutionInfo si, boolean shrinking)
	{
		this.l = l;
		this.Q = Q;
		b = (double[])b_.clone();
		y = (byte[])y_.clone();
		alpha = (double[])alpha_.clone();
		this.Cp = Cp;
		this.Cn = Cn;
		this.eps = eps;
		this.unshrinked = false;

		// initialize alpha_status
		{
			alphaStatus = new byte[l];
			for(int i=0;i<l;i++)
				updateAlphaStatus(i);
		}

		// initialize active set (for shrinking)
		{
			activeSet = new int[l];
			for(int i=0;i<l;i++)
				activeSet[i] = i;
			activeSize = l;
		}

		// initialize gradient
		{
			G = new double[l];
			GBar = new double[l];
			int i;
			for(i=0;i<l;i++)
			{
				G[i] = b[i];
				GBar[i] = 0;
			}
			for(i=0;i<l;i++)
				if(!isLowerBound(i))
				{
					float[] Q_i = Q.getQ(i,l);
					double alpha_i = alpha[i];
					int j;
					for(j=0;j<l;j++)
						G[j] += alpha_i*Q_i[j];
					if(isUpperBound(i))
						for(j=0;j<l;j++)
							GBar[j] += getC(i) * Q_i[j];
				}
		}

		// optimization step

		int iter = 0;
		int counter = Math.min(l,1000)+1;
		int[] working_set = new int[2];

		while(true)
		{
			// show progress and do shrinking

			if(--counter == 0)
			{
				counter = Math.min(l,1000);
				if(shrinking) doShrinking();
				System.err.print(".");
			}

			if(selectWorkingSet(working_set)!=0)
			{
				// reconstruct the whole gradient
				reconstructGradient();
				// reset active set size and check
				activeSize = l;
				System.err.print("*");
				if(selectWorkingSet(working_set)!=0)
					break;
				else
					counter = 1;	// do shrinking next iteration
			}
			
			int i = working_set[0];
			int j = working_set[1];

			++iter;

			// update alpha[i] and alpha[j], handle bounds carefully

			float[] Q_i = Q.getQ(i,activeSize);
			float[] Q_j = Q.getQ(j,activeSize);

			double C_i = getC(i);
			double C_j = getC(j);

			double old_alpha_i = alpha[i];
			double old_alpha_j = alpha[j];

			if(y[i]!=y[j])
			{
				double delta = (-G[i]-G[j])/Math.max(Q_i[i]+Q_j[j]+2*Q_i[j],(float)0);
				double diff = alpha[i] - alpha[j];
				alpha[i] += delta;
				alpha[j] += delta;
			
				if(diff > 0)
				{
					if(alpha[j] < 0)
					{
						alpha[j] = 0;
						alpha[i] = diff;
					}
				}
				else
				{
					if(alpha[i] < 0)
					{
						alpha[i] = 0;
						alpha[j] = -diff;
					}
				}
				if(diff > C_i - C_j)
				{
					if(alpha[i] > C_i)
					{
						alpha[i] = C_i;
						alpha[j] = C_i - diff;
					}
				}
				else
				{
					if(alpha[j] > C_j)
					{
						alpha[j] = C_j;
						alpha[i] = C_j + diff;
					}
				}
			}
			else
			{
				double delta = (G[i]-G[j])/Math.max(Q_i[i]+Q_j[j]-2*Q_i[j],(float)0);
				double sum = alpha[i] + alpha[j];
				alpha[i] -= delta;
				alpha[j] += delta;
				if(sum > C_i)
				{
					if(alpha[i] > C_i)
					{
						alpha[i] = C_i;
						alpha[j] = sum - C_i;
					}
				}
				else
				{
					if(alpha[j] < 0)
					{
						alpha[j] = 0;
						alpha[i] = sum;
					}
				}
				if(sum > C_j)
				{
					if(alpha[j] > C_j)
					{
						alpha[j] = C_j;
						alpha[i] = sum - C_j;
					}
				}
				else
				{
					if(alpha[i] < 0)
					{
						alpha[i] = 0;
						alpha[j] = sum;
					}
				}
			}

			// update G

			double delta_alpha_i = alpha[i] - old_alpha_i;
			double delta_alpha_j = alpha[j] - old_alpha_j;

			for(int k=0;k<activeSize;k++)
			{
				G[k] += Q_i[k]*delta_alpha_i + Q_j[k]*delta_alpha_j;
			}

			// update alpha_status and G_bar

			{
				boolean ui = isUpperBound(i);
				boolean uj = isUpperBound(j);
				updateAlphaStatus(i);
				updateAlphaStatus(j);
				int k;
				if(ui != isUpperBound(i))
				{
					Q_i = Q.getQ(i,l);
					if(ui)
						for(k=0;k<l;k++)
							GBar[k] -= C_i * Q_i[k];
					else
						for(k=0;k<l;k++)
							GBar[k] += C_i * Q_i[k];
				}

				if(uj != isUpperBound(j))
				{
					Q_j = Q.getQ(j,l);
					if(uj)
						for(k=0;k<l;k++)
							GBar[k] -= C_j * Q_j[k];
					else
						for(k=0;k<l;k++)
							GBar[k] += C_j * Q_j[k];
				}
			}

		}

		// calculate rho

		si.rho = calculateRho();

		// calculate objective value
		{
			double v = 0;
			int i;
			for(i=0;i<l;i++)
				v += alpha[i] * (G[i] + b[i]);

			si.obj = v/2;
		}

		// put back the solution
		{
			for(int i=0;i<l;i++)
				alpha_[activeSet[i]] = alpha[i];
		}

		si.upper_bound_p = Cp;
		si.upper_bound_n = Cn;

		System.out.print("\noptimization finished, #iter = "+iter+"\n");
	}

	// return 1 if already optimal, return 0 otherwise
	int selectWorkingSet(int[] working_set)
	{
		// return i,j which maximize -grad(f)^T d , under constraint
		// if alpha_i == C, d != +1
		// if alpha_i == 0, d != -1

		double Gmax1 = -INF;		// max { -grad(f)_i * d | y_i*d = +1 }
		int Gmax1_idx = -1;

		double Gmax2 = -INF;		// max { -grad(f)_i * d | y_i*d = -1 }
		int Gmax2_idx = -1;

		for(int i=0;i<activeSize;i++)
		{
			if(y[i]==+1)	// y = +1
			{
				if(!isUpperBound(i))	// d = +1
				{
					if(-G[i] > Gmax1)
					{
						Gmax1 = -G[i];
						Gmax1_idx = i;
					}
				}
				if(!isLowerBound(i))	// d = -1
				{
					if(G[i] > Gmax2)
					{
						Gmax2 = G[i];
						Gmax2_idx = i;
					}
				}
			}
			else		// y = -1
			{
				if(!isUpperBound(i))	// d = +1
				{
					if(-G[i] > Gmax2)
					{
						Gmax2 = -G[i];
						Gmax2_idx = i;
					}
				}
				if(!isLowerBound(i))	// d = -1
				{
					if(G[i] > Gmax1)
					{
						Gmax1 = G[i];
						Gmax1_idx = i;
					}
				}
			}
		}

		if(Gmax1+Gmax2 < eps)
			return 1;

		working_set[0] = Gmax1_idx;
		working_set[1] = Gmax2_idx;
		return 0;
	}

	void doShrinking()
	{
		int i,j,k;
		int[] working_set = new int[2];
		if(selectWorkingSet(working_set)!=0) return;
		i = working_set[0];
		j = working_set[1];
		double Gm1 = -y[j]*G[j];
		double Gm2 = y[i]*G[i];

		// shrink
	
		for(k=0;k<activeSize;k++)
		{
			if(isLowerBound(k))
			{
				if(y[k]==+1)
				{
					if(-G[k] >= Gm1) continue;
				}
				else	if(-G[k] >= Gm2) continue;
			}
			else if(isUpperBound(k))
			{
				if(y[k]==+1)
				{
					if(G[k] >= Gm2) continue;
				}
				else	if(G[k] >= Gm1) continue;
			}
			else continue;

			--activeSize;
			swapIndex(k,activeSize);
			--k;	// look at the newcomer
		}

		// unshrink, check all variables again before final iterations

		if(unshrinked || -(Gm1 + Gm2) > eps*10) return;

		unshrinked = true;
		reconstructGradient();

		for(k=l-1;k>=activeSize;k--)
		{
			if(isLowerBound(k))
			{
				if(y[k]==+1)
				{
					if(-G[k] < Gm1) continue;
				}
				else	if(-G[k] < Gm2) continue;
			}
			else if(isUpperBound(k))
			{
				if(y[k]==+1)
				{
					if(G[k] < Gm2) continue;
				}
				else	if(G[k] < Gm1) continue;
			}
			else continue;

			swapIndex(k,activeSize);
			activeSize++;
			++k;	// look at the newcomer
		}
	}

	double calculateRho()
	{
		double r;
		int nr_free = 0;
		double ub = INF, lb = -INF, sum_free = 0;
		for(int i=0;i<activeSize;i++)
		{
			double yG = y[i]*G[i];

			if(isLowerBound(i))
			{
				if(y[i] > 0)
					ub = Math.min(ub,yG);
				else
					lb = Math.max(lb,yG);
			}
			else if(isUpperBound(i))
			{
				if(y[i] < 0)
					ub = Math.min(ub,yG);
				else
					lb = Math.max(lb,yG);
			}
			else
			{
				++nr_free;
				sum_free += yG;
			}
		}

		if(nr_free>0)
			r = sum_free/nr_free;
		else
			r = (ub+lb)/2;

		return r;
	}


}
