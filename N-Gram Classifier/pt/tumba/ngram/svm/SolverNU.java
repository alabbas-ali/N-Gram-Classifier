package pt.tumba.ngram.svm;

/**
 * Solver for nu-svm classification and regression
 *
 * additional constraint: e^T \alpha = constant
 */
final class SolverNU extends Solver {
	private SolutionInfo si;

	void Solve(int l, Kernel Q, double[] b, byte[] y,
		   double[] alpha, double Cp, double Cn, double eps,
		   SolutionInfo si, boolean shrinking)
	{
		this.si = si;
		super.Solve(l,Q,b,y,alpha,Cp,Cn,eps,si,shrinking);
	}

	int selectWorkingSet(int[] working_set)
	{
		// return i,j which maximize -grad(f)^T d , under constraint
		// if alpha_i == C, d != +1
		// if alpha_i == 0, d != -1

		double Gmax1 = -INF;	// max { -grad(f)_i * d | y_i = +1, d = +1 }
		int Gmax1_idx = -1;

		double Gmax2 = -INF;	// max { -grad(f)_i * d | y_i = +1, d = -1 }
		int Gmax2_idx = -1;

		double Gmax3 = -INF;	// max { -grad(f)_i * d | y_i = -1, d = +1 }
		int Gmax3_idx = -1;

		double Gmax4 = -INF;	// max { -grad(f)_i * d | y_i = -1, d = -1 }
		int Gmax4_idx = -1;

		for(int i=0;i<activeSize;i++)
		{
			if(y[i]==+1)	// y == +1
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
			else		// y == -1
			{
				if(!isUpperBound(i))	// d = +1
				{
					if(-G[i] > Gmax3)
					{
						Gmax3 = -G[i];
						Gmax3_idx = i;
					}
				}
				if(!isLowerBound(i))	// d = -1
				{
					if(G[i] > Gmax4)
					{
						Gmax4 = G[i];
						Gmax4_idx = i;
					}
				}
			}
		}

		if(Math.max(Gmax1+Gmax2,Gmax3+Gmax4) < eps)
			return 1;

		if(Gmax1+Gmax2 > Gmax3+Gmax4)
		{
			working_set[0] = Gmax1_idx;
			working_set[1] = Gmax2_idx;
		}
		else
		{
			working_set[0] = Gmax3_idx;
			working_set[1] = Gmax4_idx;
		}
		return 0;
	}

	void doShrinking()
	{
		double Gmax1 = -INF;	// max { -grad(f)_i * d | y_i = +1, d = +1 }
		double Gmax2 = -INF;	// max { -grad(f)_i * d | y_i = +1, d = -1 }
		double Gmax3 = -INF;	// max { -grad(f)_i * d | y_i = -1, d = +1 }
		double Gmax4 = -INF;	// max { -grad(f)_i * d | y_i = -1, d = -1 }

		int k;
		for(k=0;k<activeSize;k++)
		{
			if(!isUpperBound(k))
			{
				if(y[k]==+1)
				{
					if(-G[k] > Gmax1) Gmax1 = -G[k];
				}
				else	if(-G[k] > Gmax3) Gmax3 = -G[k];
			}
			if(!isLowerBound(k))
			{
				if(y[k]==+1)
				{	
					if(G[k] > Gmax2) Gmax2 = G[k];
				}
				else	if(G[k] > Gmax4) Gmax4 = G[k];
			}
		}

		double Gm1 = -Gmax2;
		double Gm2 = -Gmax1;
		double Gm3 = -Gmax4;
		double Gm4 = -Gmax3;

		for(k=0;k<activeSize;k++)
		{
			if(isLowerBound(k))
			{
				if(y[k]==+1)
				{
					if(-G[k] >= Gm1) continue;
				}
				else	if(-G[k] >= Gm3) continue;
			}
			else if(isUpperBound(k))
			{
				if(y[k]==+1)
				{
					if(G[k] >= Gm2) continue;
				}
				else	if(G[k] >= Gm4) continue;
			}
			else continue;

			--activeSize;
			swapIndex(k,activeSize);
			--k;	// look at the newcomer
		}

		// unshrink, check all variables again before final iterations

		if(unshrinked || Math.max(-(Gm1+Gm2),-(Gm3+Gm4)) > eps*10) return;
	
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
				else	if(-G[k] < Gm3) continue;
			}
			else if(isUpperBound(k))
			{
				if(y[k]==+1)
				{
					if(G[k] < Gm2) continue;
				}
				else	if(G[k] < Gm4) continue;
			}
			else continue;

			swapIndex(k,activeSize);
			activeSize++;
			++k;	// look at the newcomer
		}
	}
	
	double calculateRho()
	{
		int nr_free1 = 0,nr_free2 = 0;
		double ub1 = INF, ub2 = INF;
		double lb1 = -INF, lb2 = -INF;
		double sum_free1 = 0, sum_free2 = 0;

		for(int i=0;i<activeSize;i++)
		{
			if(y[i]==+1)
			{
				if(isLowerBound(i))
					ub1 = Math.min(ub1,G[i]);
				else if(isUpperBound(i))
					lb1 = Math.max(lb1,G[i]);
				else
				{
					++nr_free1;
					sum_free1 += G[i];
				}
			}
			else
			{
				if(isLowerBound(i))
					ub2 = Math.min(ub2,G[i]);
				else if(isUpperBound(i))
					lb2 = Math.max(lb2,G[i]);
				else
				{
					++nr_free2;
					sum_free2 += G[i];
				}
			}
		}

		double r1,r2;
		if(nr_free1 > 0)
			r1 = sum_free1/nr_free1;
		else
			r1 = (ub1+lb1)/2;

		if(nr_free2 > 0)
			r2 = sum_free2/nr_free2;
		else
			r2 = (ub2+lb2)/2;

		si.r = (r1+r2)/2;
		return (r1-r2)/2;
	}
}
