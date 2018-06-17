package pt.tumba.ngram.svm;

import java.io.*;
import java.util.*;

/**
 * Construct and solve various formulations of the support vector machine (SVM) problem.</p><p>
 * 
 * A support vector machine is a supervised learning algorithm developed over the past
 * decade by Vapnik and others (Vapnik, Statistical Learning Theory, 1998). The algorithm
 * addresses the general problem of learning to discriminate between positive and negative
 * members of a given class of n-dimensional vectors.</p><p> 
 * 
 * The SVM algorithm operates by mapping the given training set into a possibly
 *  high-dimensional feature space and attempting to locate in that space a plane that 
 * separates the positive from the negative examples. Having found such a plane, the 
 * SVM can then predict the classification of an unlabeled example by mapping it 
 * into the feature space and asking on which side of the separating plane the example lies.
 *  Much of the SVM's power comes from its criterion for selecting a separating plane 
 * when many candidates planes exist: the SVM chooses the plane that maintains a 
 * maximum margin from any point in the training set. Statistical learning theory suggests 
 * that, for some classes of well-behaved data, the choice of the maximum margin 
 * hyperplane will lead to maximal generalization when predicting the classification of 
 * previously unseen examples (Vapnik, Statistical Learning Theory, 1998). The SVM 
 * algorithm can also be extended to cope with noise in the training set and with multiple 
 * classes (Cristianini and Shawe-Taylor, An Introduction to Support Vector Machines, 2000).</p><p>
 * 
 * Say that we have a training data set containing n examples, each of which is a vector of
 * m numbers. These vectors may be thought of as points in an m-dimensional space. In
 * theory, a simple way to build a binary classifier is to construct a hyperplane (i.e., a 
 * plane in a space with more than three dimensions) separating class members (positive
 * examples) from non-members (negative examples) in this space. Unfortunately, most 
 * real-world problems involve non-separable data for which there does not exist a hyperplane
 *  that successfully separates the positive from the negative examples. One solution to the
 *  inseparability problem is to map the data into a higher-dimensional space and define a
 *  separating hyperplane there. This higher-dimensional space is called the feature space, 
 * as opposed to the input space occupied by the training examples. With an appropriately 
 * chosen feature space of sufficient dimensionality, any consistent training set can be made 
 * separable. However, translating the training set into a higher-dimensional space incurs
 *  both computational and learning-theoretic costs. Furthermore, artificially separating the 
 * data in this way exposes the learning system to the risk of finding trivial solutions that 
 * overfit the data.</p><p>
 *
 * SVMs elegantly sidestep both difficulties. They avoid overfitting by choosing the maximum
 * margin separating hyperplane from among the many that can separate the positive from 
 * negative examples in the feature space. Also, the decision function for classifying points
 * with respect to the hyperplane only involves dot products between points in the feature 
 * space. Because the algorithm that finds a separating hyperplane in the feature space 
 * can be stated entirely in terms of vectors in the input space and dot products in the
 *  feature space, a support vector machine can locate the hyperplane without ever 
 * representing the space explicitly, simply by defining a function, called a kernel function, 
 * that plays the role of the dot product in the feature space. This technique avoids the
 * computational burden of explicitly representing the feature vectors.</p><p>
 *
 * For some data sets, the SVM may not be able to find a separating hyperplane in feature
 *  space, either because the kernel function is inappropriate for the training data or because 
 * the data contains mislabeled examples. The latter problem can be addressed by using a
 *  soft margin that allows some training examples to fall on the wrong side of the separating 
 * hyperplane. Completely specifying a support vector machine therefore requires specifying
 *  two parameters: the kernel function and the magnitude of the penalty for violating the soft
 *  margin. The settings of these parameters depend on the specific data at hand.</p>
 * 
 * Given an expression vector X for each example, the simplest kernel K(X,Y) that we 
 * can use to measure the similarity between vectors X and Y is the dot product in the 
 * input space K(X,Y) = X ° Y. For technical reasons (namely, that our optimization 
 * algorithm requires that the hyperplane pass through the origin), we typically add a 
 * constant to this kernel, obtaining a kernel such as by K(X,Y) = X ° Y +1. When this 
 * dot product kernel is used, the feature space is essentially the same as the 
 * m-dimensional input space, and the SVM will classify the examples with a 
 * separating hyperplane in this space. Squaring this kernel, i.e. defining K(X,Y) = (X ° Y +1)2, 
 * yields a quadratic separating surface in the input space. The corresponding separating 
 * hyperplane in the feature space includes features for all pairwise interactions Xi Xj 
 * between features, where 1 <= i, j <= m. Raising the kernel to higher powers yields 
 * polynomial separating surfaces of higher degrees in the input space. In general, the
 * kernel of degree d is defined by K(X,Y) = (X ° Y +1)d. In the feature space of this kernel, 
 * for any example X there are features for all d-fold interactions between features.</p><p>
 *
 * Also commonly employed is the radial basis kernel (Scholkopf et al., IEEE Trans Sig 
 * Proc 45(11):2758-2765), which has a Gaussian form K(X,Y) = exp(-||X - Y||2 / 2 s2), 
 * where s is the width of the Gaussian. In the SVM, s is usually set equal to the median 
 * of the Euclidean distances from each positive example to the nearest negative example.</p><p>
 *
 * In many real-world problems, one of the two classes examined contains very few 
 * members relative to the other class. This imbalance in the number of positive and 
 * negative training examples, in combination with noise in the data, is likely to cause the 
 * SVM to make incorrect classifications. When the magnitude of the noise in the negative 
 * examples outweighs the total number of positive examples, the optimal hyperplane located 
 * by the SVM will be uninformative, classifying all members of the training set as negative 
 * examples. We combat this problem by modifying the matrix of kernel values computed 
 * during SVM optimization. Let X(1), ..., X(n) be the examples in the training set, and let 
 * K be the matrix defined by the kernel function K on this training set; i.e., Kij = K(X(i),X(j)). 
 * By adding to the diagonal of the kernel matrix a constant whose magnitude depends on the 
 * class of the training example, one can control the fraction of misclassified points in the two 
 * classes. This technique ensures that the positive points are not regarded as noisy labels. 
 * For positive examples, the diagonal element is modified by Kij := Kij + f*r*(n+/N), 
 * where n+ is the number of positive training examples, N is the total number of 
 * training examples, f is a user-specified scale factor, and r is the median diagonal 
 * kernel element. A similar formula is used for the negative examples, with n+ replaced 
 * by n-.</p><p>
 *
 *  A more mathematically detailed discussion of these techniques is available at 
 * <a href="http://www.cse.ucsc.edu/research/compbio/genex">http://www.cse.ucsc.edu/research/compbio/genex</a>.
 * 
 * @author Bruno Martins
 */
public class SVM {

	/**
	 * Solve the C-Support Vector classification problem.
	 * 
	 * @param prob The SVM Problem.
	 * @param param The parameters.
	 * @param alpha The SVM alpha values (will be filled by learning).
	 * @param si Information about the solution (used for returning multiple values). 
	 * @param Cp
	 * @param Cn
	 */
	private static void solveCSVC(SVMProblem prob, SVMParameter param,
					double[] alpha, Solver.SolutionInfo si,
					double Cp, double Cn)
	{
		int l = prob.l;
		double[] minus_ones = new double[l];
		byte[] y = new byte[l];

		int i;

		for(i=0;i<l;i++)
		{
			alpha[i] = 0;
			minus_ones[i] = -1;
			if(prob.y[i] > 0) y[i] = +1; else y[i]=-1;
		}

		Solver s = new Solver();
		s.Solve(l, new SVCQ(prob,param,y), minus_ones, y,
			alpha, Cp, Cn, param.eps, si, param.shrinking);

		double sum_alpha=0;
		for(i=0;i<l;i++)
			sum_alpha += alpha[i];

		if (Cp==Cn)
			System.out.print("nu = "+sum_alpha/(Cp*prob.l)+"\n");

		for(i=0;i<l;i++)
			alpha[i] *= y[i];
	}

	/**
	 * Solve the Nu-Support Vector Classification problem.
	 * 
	 * @param prob The SVM Problem.
	 * @param param The parameters.
	 * @param alpha The SVM alpha values (will be filled by learning).
	 * @param si Information about the solution (used for returning multiple values). 
	 */
	private static void solveNUSVC(SVMProblem prob, SVMParameter param,
					double[] alpha, Solver.SolutionInfo si)
	{
		int i;
		int l = prob.l;
		double nu = param.nu;

		byte[] y = new byte[l];

		for(i=0;i<l;i++)
			if(prob.y[i]>0)
				y[i] = +1;
			else
				y[i] = -1;

		double sum_pos = nu*l/2;
		double sum_neg = nu*l/2;

		for(i=0;i<l;i++)
			if(y[i] == +1)
			{
				alpha[i] = Math.min(1.0,sum_pos);
				sum_pos -= alpha[i];
			}
			else
			{
				alpha[i] = Math.min(1.0,sum_neg);
				sum_neg -= alpha[i];
			}

		double[] zeros = new double[l];

		for(i=0;i<l;i++)
			zeros[i] = 0;

		SolverNU s = new SolverNU();
		s.Solve(l, new SVCQ(prob,param,y), zeros, y,
			alpha, 1.0, 1.0, param.eps, si, param.shrinking);
		double r = si.r;

		System.out.print("C = "+1/r+"\n");

		for(i=0;i<l;i++)
			alpha[i] *= y[i]/r;

		si.rho /= r;
		si.obj /= (r*r);
		si.upper_bound_p = 1/r;
		si.upper_bound_n = 1/r;
	}

	/**
	 * Solve the SVM Distribution Estimation Problem.
	 *  
	 * @param prob The SVM Problem.
	 * @param param The parameters.
	 * @param alpha The SVM alpha values (will be filled by learning).
	 * @param si Information about the solution (used for returning multiple values). 
	 */
	private static void solveOneClass(SVMProblem prob, SVMParameter param,
						double[] alpha, Solver.SolutionInfo si)
	{
		int l = prob.l;
		double[] zeros = new double[l];
		byte[] ones = new byte[l];
		int i;

		int n = (int)(param.nu*prob.l);	// # of alpha's at upper bound

		for(i=0;i<n;i++)
			alpha[i] = 1;
		alpha[n] = param.nu * prob.l - n;
		for(i=n+1;i<l;i++)
			alpha[i] = 0;

		for(i=0;i<l;i++)
		{
			zeros[i] = 0;
			ones[i] = 1;
		}

		Solver s = new Solver();
		s.Solve(l, new ONECLASSQ(prob,param), zeros, ones,
			alpha, 1.0, 1.0, param.eps, si, param.shrinking);
	}

	/**
	 * Solve the Epsilon-Support Vector Regression Problem
	 *  
	 * @param prob The SVM Problem.
	 * @param param The parameters.
	 * @param alpha The SVM alpha values (will be filled by learning).
	 * @param si Information about the solution (used for returning multiple values). 
	 */
	private static void solveEpsilonSVR(SVMProblem prob, SVMParameter param,
					double[] alpha, Solver.SolutionInfo si)
	{
		int l = prob.l;
		double[] alpha2 = new double[2*l];
		double[] linear_term = new double[2*l];
		byte[] y = new byte[2*l];
		int i;

		for(i=0;i<l;i++)
		{
			alpha2[i] = 0;
			linear_term[i] = param.p - prob.y[i];
			y[i] = 1;

			alpha2[i+l] = 0;
			linear_term[i+l] = param.p + prob.y[i];
			y[i+l] = -1;
		}

		Solver s = new Solver();
		s.Solve(2*l, new SVRQ(prob,param), linear_term, y,
			alpha2, param.C, param.C, param.eps, si, param.shrinking);

		double sum_alpha = 0;
		for(i=0;i<l;i++)
		{
			alpha[i] = alpha2[i] - alpha2[i+l];
			sum_alpha += Math.abs(alpha[i]);
		}
		System.out.print("nu = "+sum_alpha/(param.C*l)+"\n");
	}

	/**
	 * Solve the Nu-Support Vector Regression Problem
	 *  
	 * @param prob The SVM Problem.
	 * @param param The parameters.
	 * @param alpha The SVM alpha values (will be filled by learning).
	 * @param si Information about the solution (used for returning multiple values). 
	 */
	private static void solveNUSVR(SVMProblem prob, SVMParameter param,
					double[] alpha, Solver.SolutionInfo si)
	{
		int l = prob.l;
		double C = param.C;
		double[] alpha2 = new double[2*l];
		double[] linear_term = new double[2*l];
		byte[] y = new byte[2*l];
		int i;

		double sum = C * param.nu * l / 2;
		for(i=0;i<l;i++)
		{
			alpha2[i] = alpha2[i+l] = Math.min(sum,C);
			sum -= alpha2[i];
			
			linear_term[i] = - prob.y[i];
			y[i] = 1;

			linear_term[i+l] = prob.y[i];
			y[i+l] = -1;
		}

		SolverNU s = new SolverNU();
		s.Solve(2*l, new SVRQ(prob,param), linear_term, y,
			alpha2, C, C, param.eps, si, param.shrinking);

		System.out.print("epsilon = "+(-si.r)+"\n");
		
		for(i=0;i<l;i++)
			alpha[i] = alpha2[i] - alpha2[i+l];
	}

	/** 
	 * Inner class modeling the data for the SVM decision function,
	 * used for classifying points with respect to the hyperplane.
	 */
	static class decisionFunction {
		
		/** The apha parameters. */
		double[] alpha;
		
		/** The rho parameters. */
		double rho;	
	};

	/**
	 * Computation for one step of the training procedure.
	 * 
	 * @param prob The SVM Problem
	 * @param param The Parameters
	 * @param Cp 
	 * @param Cn
	 * @return The decision function used for classifying points after this step.
	 */
	static decisionFunction svmTrainOne(SVMProblem prob, SVMParameter param, double Cp, double Cn) {
		double[] alpha = new double[prob.l];
		Solver.SolutionInfo si = new Solver.SolutionInfo();
		switch(param.svmType)
		{
			case SVMParameter.C_SVC:
				solveCSVC(prob,param,alpha,si,Cp,Cn);
				break;
			case SVMParameter.NU_SVC:
				solveNUSVC(prob,param,alpha,si);
				break;
			case SVMParameter.ONE_CLASS:
				solveOneClass(prob,param,alpha,si);
				break;
			case SVMParameter.EPSILON_SVR:
				solveEpsilonSVR(prob,param,alpha,si);
				break;
			case SVMParameter.NU_SVR:
				solveNUSVR(prob,param,alpha,si);
				break;
		}
		System.out.print("obj = "+si.obj+", rho = "+si.rho+"\n");
		// output SVs
		int nSV = 0;
		int nBSV = 0;
		for(int i=0;i<prob.l;i++)
		{
			if(Math.abs(alpha[i]) > 0)
			{
				++nSV;
				if(prob.y[i] > 0)
				{
					if(Math.abs(alpha[i]) >= si.upper_bound_p)
					++nBSV;
				}
				else
				{
					if(Math.abs(alpha[i]) >= si.upper_bound_n)
						++nBSV;
				}
			}
		}
		System.out.print("nSV = "+nSV+", nBSV = "+nBSV+"\n");
		decisionFunction f = new decisionFunction();
		f.alpha = alpha;
		f.rho = si.rho;
		return f;
	}

	/** Platt's binary SVM Probablistic Output: an improvement from Lin et al.
	 * 
	 * @param l
	 * @param dec_values
	 * @param labels
	 * @param probAB
	 */
	private static void sigmoidTrain(int l, double[] dec_values, double[] labels,  double[] probAB)
	{
		double A, B;
		double prior1=0, prior0 = 0;
		int i;

		for (i=0;i<l;i++)
			if (labels[i] > 0) prior1+=1;
			else prior0+=1;
	
		int max_iter=100; 	// Maximal number of iterations
		double min_step=1e-10;	// Minimal step taken in line search
		double sigma=1e-3;	// For numerically strict PD of Hessian
		double eps=1e-5;
		double hiTarget=(prior1+1.0)/(prior1+2.0);
		double loTarget=1/(prior0+2.0);
		double[] t= new double[l];
		double fApB,p,q,h11,h22,h21,g1,g2,det,dA,dB,gd,stepsize;
		double newA,newB,newf,d1,d2;
		int iter; 
	
		// Initial Point and Initial Fun Value
		A=0.0; B=Math.log((prior0+1.0)/(prior1+1.0));
		double fval = 0.0;

		for (i=0;i<l;i++)
		{
			if (labels[i]>0) t[i]=hiTarget;
			else t[i]=loTarget;
			fApB = dec_values[i]*A+B;
			if (fApB>=0)
				fval += t[i]*fApB + Math.log(1+Math.exp(-fApB));
			else
				fval += (t[i] - 1)*fApB +Math.log(1+Math.exp(fApB));
		}
		for (iter=0;iter<max_iter;iter++)
		{
			// Update Gradient and Hessian (use H' = H + sigma I)
			h11=sigma; // numerically ensures strict PD
			h22=sigma;
			h21=0.0;g1=0.0;g2=0.0;
			for (i=0;i<l;i++)
			{
				fApB = dec_values[i]*A+B;
				if (fApB >= 0)
				{
					p=Math.exp(-fApB)/(1.0+Math.exp(-fApB));
					q=1.0/(1.0+Math.exp(-fApB));
				}
				else
				{
					p=1.0/(1.0+Math.exp(fApB));
					q=Math.exp(fApB)/(1.0+Math.exp(fApB));
				}
				d2=p*q;
				h11+=dec_values[i]*dec_values[i]*d2;
				h22+=d2;
				h21+=dec_values[i]*d2;
				d1=t[i]-p;
				g1+=dec_values[i]*d1;
				g2+=d1;
			}

			// Stopping Criteria
			if (Math.abs(g1)<eps && Math.abs(g2)<eps)
				break;
			
			// Finding Newton direction: -inv(H') * g
			det=h11*h22-h21*h21;
			dA=-(h22*g1 - h21 * g2) / det;
			dB=-(-h21*g1+ h11 * g2) / det;
			gd=g1*dA+g2*dB;


			stepsize = 1; 		// Line Search
			while (stepsize >= min_step)
			{
				newA = A + stepsize * dA;
				newB = B + stepsize * dB;

				// New function value
				newf = 0.0;
				for (i=0;i<l;i++)
				{
					fApB = dec_values[i]*newA+newB;
					if (fApB >= 0)
						newf += t[i]*fApB + Math.log(1+Math.exp(-fApB));
					else
						newf += (t[i] - 1)*fApB +Math.log(1+Math.exp(fApB));
				}
				// Check sufficient decrease
				if (newf<fval+0.0001*stepsize*gd)
				{
					A=newA;B=newB;fval=newf;
					break;
				}
				else
					stepsize = stepsize / 2.0;
			}
			
			if (stepsize < min_step)
			{
				System.err.print("Line search fails in two-class probability estimates\n");
				break;
			}
		}
		
		if (iter>=max_iter)
			System.err.print("Reaching maximal iterations in two-class probability estimates\n");
		probAB[0]=A;probAB[1]=B;
	}

	/**
	 * 
	 * @param decision_value
	 * @param A
	 * @param B
	 * @return
	 */
	private static double sigmoidPredict(double decision_value, double A, double B)
	{
		double fApB = decision_value*A+B;
		if (fApB >= 0)
			return Math.exp(-fApB)/(1.0+Math.exp(-fApB));
		else
			return 1.0/(1+Math.exp(fApB)) ;
	}

	/** Method 2 from the multiclass_prob paper by Wu, Lin, and Weng
	 * 
	 * @param k
	 * @param r
	 * @param p
	 */
	private static void multiclassProbability(int k, double[][] r, double[] p)
	{
		int t;
		int iter = 0, max_iter=100;
		double[][] Q=new double[k][k];
		double[] Qp= new double[k];
		double pQp, eps=0.001;
	
		for (t=0;t<k;t++)
		{
			p[t]=1.0/k;  // Valid if k = 1
			Q[t][t]=0;
			for (int j=0;j<t;j++)
			{
				Q[t][t]+=r[j][t]*r[j][t];
				Q[t][j]=Q[j][t];
			}
			for (int j=t+1;j<k;j++)
			{
				Q[t][t]+=r[j][t]*r[j][t];
				Q[t][j]=-r[j][t]*r[t][j];
			}
		}
		for (iter=0;iter<max_iter;iter++)
		{
			// stopping condition, recalculate QP,pQP for numerical accuracy
			pQp=0;
			for (t=0;t<k;t++)
			{
				Qp[t]=0;
				for (int j=0;j<k;j++)
					Qp[t]+=Q[t][j]*p[j];
				pQp+=p[t]*Qp[t];
			}
			double max_error=0;
			for (t=0;t<k;t++)
			{
				double error=Math.abs(Qp[t]-pQp);
				if (error>max_error)
					max_error=error;
			}
			if (max_error<eps) break;
		
			for (t=0;t<k;t++)
			{
				double diff=(-Qp[t]+pQp)/Q[t][t];
				p[t]+=diff;
				pQp=(pQp+diff*(diff*Q[t][t]+2*Qp[t]))/(1+diff)/(1+diff);
				for (int j=0;j<k;j++)
				{
					Qp[j]=(Qp[j]+diff*Q[t][j])/(1+diff);
					p[j]/=(1+diff);
				}
			}
		}
		if (iter>=max_iter)
			System.err.print("Exceeds max_iter in multiclass_prob\n");
	}

	/** Cross-validation decision values for probability estimates.
	 * 
	 * @param prob
	 * @param param
	 * @param Cp
	 * @param Cn
	 * @param probAB
	 */
	private static void svmBinarySVCProbability(SVMProblem prob, SVMParameter param, double Cp, double Cn, double[] probAB)
	{
		int i;
		int nr_fold = 5;
		int[] perm = new int[prob.l];
		double[] dec_values = new double[prob.l];

		// random shuffle
		for(i=0;i<prob.l;i++) perm[i]=i;
		for(i=0;i<prob.l;i++)
		{
			int j = i+(int)(Math.random()*(prob.l-i));
			do {int _=perm[i]; perm[i]=perm[j]; perm[j]=_;} while(false);
		}
		for(i=0;i<nr_fold;i++)
		{
			int begin = i*prob.l/nr_fold;
			int end = (i+1)*prob.l/nr_fold;
			int j,k;
			SVMProblem subprob = new SVMProblem();

			subprob.l = prob.l-(end-begin);
			subprob.x = new SVMNode[subprob.l][];
			subprob.y = new double[subprob.l];
			
			k=0;
			for(j=0;j<begin;j++)
			{
				subprob.x[k] = prob.x[perm[j]];
				subprob.y[k] = prob.y[perm[j]];
				++k;
			}
			for(j=end;j<prob.l;j++)
			{
				subprob.x[k] = prob.x[perm[j]];
				subprob.y[k] = prob.y[perm[j]];
				++k;
			}
			int p_count=0,n_count=0;
			for(j=0;j<k;j++)
				if(subprob.y[j]>0)
					p_count++;
				else
					n_count++;
			
			if(p_count==0 && n_count==0)
				for(j=begin;j<end;j++)
					dec_values[perm[j]] = 0;
			else if(p_count > 0 && n_count == 0)
				for(j=begin;j<end;j++)
					dec_values[perm[j]] = 1;
			else if(p_count == 0 && n_count > 0)
				for(j=begin;j<end;j++)
					dec_values[perm[j]] = -1;
			else
			{
				SVMParameter subparam = (SVMParameter)param.clone();
				subparam.probability=false;
				subparam.C=1.0;
				subparam.nrWeight=2;
				subparam.weightLabel = new int[2];
				subparam.weight = new double[2];
				subparam.weightLabel[0]=+1;
				subparam.weightLabel[1]=-1;
				subparam.weight[0]=Cp;
				subparam.weight[1]=Cn;
				SVMModel submodel = svmTrain(subprob,subparam);
				for(j=begin;j<end;j++)
				{
					double[] dec_value=new double[1];
					svmPredictValues(submodel,prob.x[perm[j]],dec_value);
					dec_values[perm[j]]=dec_value[0];
					// ensure +1 -1 order; reason not using CV subroutine
					dec_values[perm[j]] *= submodel.label[0];
				}		
			}
		}		
		sigmoidTrain(prob.l,dec_values,prob.y,probAB);
	}

	/** Return parameter of a Laplace distribution
	 * 
	 * @param prob
	 * @param param
	 * @return
	 */ 
	private static double svmSVRProbability(SVMProblem prob, SVMParameter param)
	{
		int i;
		int nr_fold = 5;
		double[] ymv = new double[prob.l];
		double mae = 0;

		SVMParameter newparam = (SVMParameter)param.clone();
		newparam.probability = false;
		svmCrossValidation(prob,newparam,nr_fold,ymv);
		for(i=0;i<prob.l;i++)
		{
			ymv[i]=prob.y[i]-ymv[i];
			mae += Math.abs(ymv[i]);
		}		
		mae /= prob.l;
		double std=Math.sqrt(2*mae*mae);
		int count=0;
		mae=0;
		for(i=0;i<prob.l;i++)
			if (Math.abs(ymv[i]) > 5*std) 
				count=count+1;
			else 
				mae+=Math.abs(ymv[i]);
		mae /= (prob.l-count);
		System.err.print("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="+mae+"\n");
		return mae;
	}

	/**
	 *  Train the SVM.
	 * 
	 * @param prob The SVM Problem.
	 * @param param The parameters.
	 * @return An SVM model for the given problem.
	 */
	public static SVMModel svmTrain(SVMProblem prob, SVMParameter param)
	{
		SVMModel model = new SVMModel();
		model.params = param;

		if(param.svmType == SVMParameter.ONE_CLASS ||
		   param.svmType == SVMParameter.EPSILON_SVR ||
		   param.svmType == SVMParameter.NU_SVR)
		{
			// regression or one-class-svm
			model.nrClasses = 2;
			model.label = null;
			model.numSupportVectors = null;
			model.probA = null; model.probB = null;
			model.supportVectorsCoef = new double[1][];

			if(param.probability &&
			   (param.svmType == SVMParameter.EPSILON_SVR ||
				param.svmType == SVMParameter.NU_SVR))
			{
				model.probA = new double[1];
				model.probA[0] = svmSVRProbability(prob,param);
			}

			decisionFunction f = svmTrainOne(prob,param,0,0);
			model.rho = new double[1];
			model.rho[0] = f.rho;

			int nSV = 0;
			int i;
			for(i=0;i<prob.l;i++)
				if(Math.abs(f.alpha[i]) > 0) ++nSV;
			model.totalNrSupportVectors = nSV;
			model.supportVectors = new SVMNode[nSV][];
			model.supportVectorsCoef[0] = new double[nSV];
			int j = 0;
			for(i=0;i<prob.l;i++)
				if(Math.abs(f.alpha[i]) > 0)
				{
					model.supportVectors[j] = prob.x[i];
					model.supportVectorsCoef[0][j] = f.alpha[i];
					++j;
				}		
		}
		else
		{
			// classification
			// find out the number of classes
			int l = prob.l;
			int max_nr_class = 16;
			int nr_class = 0;
			int[] label = new int[max_nr_class];
			int[] count = new int[max_nr_class];
			int[] index = new int[l];

			int i;
			for(i=0;i<l;i++)
			{
				int this_label = (int)prob.y[i];
				int j;
				for(j=0;j<nr_class;j++)
					if(this_label == label[j])
					{
						++count[j];
						break;
					}
				index[i] = j;
				if(j == nr_class)
				{
					if(nr_class == max_nr_class)
					{
						max_nr_class *= 2;
						int[] new_data = new int[max_nr_class];
						System.arraycopy(label,0,new_data,0,label.length);
						label = new_data;
						
						new_data = new int[max_nr_class];
						System.arraycopy(count,0,new_data,0,count.length);
						count = new_data;
					}
					label[nr_class] = this_label;
					count[nr_class] = 1;
					++nr_class;
				}
			}

			// group training data of the same class

			int[] start = new int[nr_class];
			start[0] = 0;
			for(i=1;i<nr_class;i++)
				start[i] = start[i-1]+count[i-1];

			SVMNode[][] x = new SVMNode[l][];
		
			for(i=0;i<l;i++)
			{
				x[start[index[i]]] = prob.x[i];
				++start[index[i]];
			}
		
			start[0] = 0;
			for(i=1;i<nr_class;i++)
				start[i] = start[i-1]+count[i-1];

			// calculate weighted C

			double[] weighted_C = new double[nr_class];
			for(i=0;i<nr_class;i++)
				weighted_C[i] = param.C;
			for(i=0;i<param.nrWeight;i++)
			{	
				int j;
				for(j=0;j<nr_class;j++)
					if(param.weightLabel[i] == label[j])
						break;
				if(j == nr_class)
					System.err.print("warning: class label "+param.weightLabel[i]+" specified in weight is not found\n");
				else
					weighted_C[j] *= param.weight[i];
			}

			// train k*(k-1)/2 models
		
			boolean[] nonzero = new boolean[l];
			for(i=0;i<l;i++)
				nonzero[i] = false;
			decisionFunction[] f = new decisionFunction[nr_class*(nr_class-1)/2];

			double[] probA=null,probB=null;
			if (param.probability)
			{
				probA=new double[nr_class*(nr_class-1)/2];
				probB=new double[nr_class*(nr_class-1)/2];
			}

			int p = 0;
			for(i=0;i<nr_class;i++)
				for(int j=i+1;j<nr_class;j++)
				{
					SVMProblem sub_prob = new SVMProblem();
					int si = start[i], sj = start[j];
					int ci = count[i], cj = count[j];
					sub_prob.l = ci+cj;
					sub_prob.x = new SVMNode[sub_prob.l][];
					sub_prob.y = new double[sub_prob.l];
					int k;
					for(k=0;k<ci;k++)
					{
						sub_prob.x[k] = x[si+k];
						sub_prob.y[k] = +1;
					}
					for(k=0;k<cj;k++)
					{
						sub_prob.x[ci+k] = x[sj+k];
						sub_prob.y[ci+k] = -1;
					}
				
					if(param.probability)
					{
						double[] probAB=new double[2];
						svmBinarySVCProbability(sub_prob,param,weighted_C[i],weighted_C[j],probAB);
						probA[p]=probAB[0];
						probB[p]=probAB[1];
					}

					f[p] = svmTrainOne(sub_prob,param,weighted_C[i],weighted_C[j]);
					for(k=0;k<ci;k++)
						if(!nonzero[si+k] && Math.abs(f[p].alpha[k]) > 0)
							nonzero[si+k] = true;
					for(k=0;k<cj;k++)
						if(!nonzero[sj+k] && Math.abs(f[p].alpha[ci+k]) > 0)
							nonzero[sj+k] = true;
					++p;
				}

			// build output

			model.nrClasses = nr_class;
		
			model.label = new int[nr_class];
			for(i=0;i<nr_class;i++)
				model.label[i] = label[i];
		
			model.rho = new double[nr_class*(nr_class-1)/2];
			for(i=0;i<nr_class*(nr_class-1)/2;i++)
				model.rho[i] = f[i].rho;

			if(param.probability)
			{
				model.probA = new double[nr_class*(nr_class-1)/2];
				model.probB = new double[nr_class*(nr_class-1)/2];
				for(i=0;i<nr_class*(nr_class-1)/2;i++)
				{
					model.probA[i] = probA[i];
					model.probB[i] = probB[i];
				}
			}
			else
			{
				model.probA=null;
				model.probB=null;
			}

			int nnz = 0;
			int[] nz_count = new int[nr_class];
			model.numSupportVectors = new int[nr_class];
			for(i=0;i<nr_class;i++)
			{
				int nSV = 0;
				for(int j=0;j<count[i];j++)
					if(nonzero[start[i]+j])
					{	
						++nSV;
						++nnz;
					}
				model.numSupportVectors[i] = nSV;
				nz_count[i] = nSV;
			}
		
			System.out.print("Total nSV = "+nnz+"\n");

			model.totalNrSupportVectors = nnz;
			model.supportVectors = new SVMNode[nnz][];
			p = 0;
			for(i=0;i<l;i++)
				if(nonzero[i]) model.supportVectors[p++] = x[i];

			int[] nz_start = new int[nr_class];
			nz_start[0] = 0;
			for(i=1;i<nr_class;i++)
				nz_start[i] = nz_start[i-1]+nz_count[i-1];

			model.supportVectorsCoef = new double[nr_class-1][];
			for(i=0;i<nr_class-1;i++)
				model.supportVectorsCoef[i] = new double[nnz];

			p = 0;
			for(i=0;i<nr_class;i++)
				for(int j=i+1;j<nr_class;j++)
				{
					// classifier (i,j): coefficients with
					// i are in sv_coef[j-1][nz_start[i]...],
					// j are in sv_coef[i][nz_start[j]...]

					int si = start[i];
					int sj = start[j];
					int ci = count[i];
					int cj = count[j];
				
					int q = nz_start[i];
					int k;
					for(k=0;k<ci;k++)
						if(nonzero[si+k])
							model.supportVectorsCoef[j-1][q++] = f[p].alpha[k];
					q = nz_start[j];
					for(k=0;k<cj;k++)
						if(nonzero[sj+k])
							model.supportVectorsCoef[i][q++] = f[p].alpha[ci+k];
					++p;
				}
		}
		return model;
	}

	/**
	 * Perform cross validation
	 * 
	 * @param prob The SVM problem.
	 * @param param The parameters.
	 * @param nr_fold Number of folds for the cross validation.
	 * @param target
	 */
	public static void svmCrossValidation(SVMProblem prob, SVMParameter param, int nr_fold, double[] target)
	{
		int i;
		int[] perm = new int[prob.l];		

		// random shuffle
		for(i=0;i<prob.l;i++) perm[i]=i;
		for(i=0;i<prob.l;i++)
		{
			int j = i+(int)(Math.random()*(prob.l-i));
			do {int _=perm[i]; perm[i]=perm[j]; perm[j]=_;} while(false);
		}
		for(i=0;i<nr_fold;i++)
		{
			int begin = i*prob.l/nr_fold;
			int end = (i+1)*prob.l/nr_fold;
			int j,k;
			SVMProblem subprob = new SVMProblem();

			subprob.l = prob.l-(end-begin);
			subprob.x = new SVMNode[subprob.l][];
			subprob.y = new double[subprob.l];

			k=0;
			for(j=0;j<begin;j++)
			{
				subprob.x[k] = prob.x[perm[j]];
				subprob.y[k] = prob.y[perm[j]];
				++k;
			}
			for(j=end;j<prob.l;j++)
			{
				subprob.x[k] = prob.x[perm[j]];
				subprob.y[k] = prob.y[perm[j]];
				++k;
			}
			SVMModel submodel = svmTrain(subprob,param);
			if(param.probability  &&
			   (param.svmType == SVMParameter.C_SVC ||
				param.svmType == SVMParameter.NU_SVC))
			{
				double[] prob_estimates= new double[submodel.nrClasses];
				for(j=begin;j<end;j++)
					target[perm[j]] = svmPredictProbability(submodel,prob.x[perm[j]],prob_estimates);
			}
			else
				for(j=begin;j<end;j++)
					target[perm[j]] = svmPredict(submodel,prob.x[perm[j]]);
		}
	}

	/**
	 * 
	 * @param model
	 * @param label
	 */	  
	public static void svmGetLabels(SVMModel model, int[] label)
	{
		if (model.label != null)
			for(int i=0;i<model.nrClasses;i++)
				label[i] = model.label[i];
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	public static double svmGetSVRProbability(SVMModel model) {
		if ((model.params.svmType == SVMParameter.EPSILON_SVR || model.params.svmType == SVMParameter.NU_SVR) &&
			model.probA!=null)
		return model.probA[0];
		else
		{
			System.err.print("Model doesn't contain information for SVR probability inference\n");
			return 0;
		}
	}

	/**
	 * 
	 * @param model
	 * @param x
	 * @param dec_values
	 */
	public static void svmPredictValues(SVMModel model, SVMNode[] x, double[] dec_values)
	{
		if(model.params.svmType == SVMParameter.ONE_CLASS ||
		   model.params.svmType == SVMParameter.EPSILON_SVR ||
		   model.params.svmType == SVMParameter.NU_SVR)
		{
			double[] sv_coef = model.supportVectorsCoef[0];
			double sum = 0;
			for(int i=0;i<model.totalNrSupportVectors;i++)
				sum += sv_coef[i] * Kernel.kernelFunction(x,model.supportVectors[i],model.params);
			sum -= model.rho[0];
			dec_values[0] = sum;
		}
		else
		{
			int i;
			int nr_class = model.nrClasses;
			int l = model.totalNrSupportVectors;
		
			double[] kvalue = new double[l];
			for(i=0;i<l;i++)
				kvalue[i] = Kernel.kernelFunction(x,model.supportVectors[i],model.params);

			int[] start = new int[nr_class];
			start[0] = 0;
			for(i=1;i<nr_class;i++)
				start[i] = start[i-1]+model.numSupportVectors[i-1];

			int p=0;
			int pos=0;
			for(i=0;i<nr_class;i++)
				for(int j=i+1;j<nr_class;j++)
				{
					double sum = 0;
					int si = start[i];
					int sj = start[j];
					int ci = model.numSupportVectors[i];
					int cj = model.numSupportVectors[j];
				
					int k;
					double[] coef1 = model.supportVectorsCoef[j-1];
					double[] coef2 = model.supportVectorsCoef[i];
					for(k=0;k<ci;k++)
						sum += coef1[si+k] * kvalue[si+k];
					for(k=0;k<cj;k++)
						sum += coef2[sj+k] * kvalue[sj+k];
					sum -= model.rho[p++];
					dec_values[pos++] = sum;					
				}
		}
	}

	/**
	 * 
	 * @param model
	 * @param x
	 * @return
	 */
	public static double svmPredict(SVMModel model, SVMNode[] x)
	{
		if(model.params.svmType == SVMParameter.ONE_CLASS ||
		   model.params.svmType == SVMParameter.EPSILON_SVR ||
		   model.params.svmType == SVMParameter.NU_SVR)
		{
			double[] res = new double[1];
			svmPredictValues(model, x, res);

			if(model.params.svmType == SVMParameter.ONE_CLASS)
				return (res[0]>0)?1:-1;
			else
				return res[0];
		}
		else
		{
			int i;
			int nr_class = model.nrClasses;
			double[] dec_values = new double[nr_class*(nr_class-1)/2];
			svmPredictValues(model, x, dec_values);

			int[] vote = new int[nr_class];
			for(i=0;i<nr_class;i++)
				vote[i] = 0;
			int pos=0;
			for(i=0;i<nr_class;i++)
				for(int j=i+1;j<nr_class;j++)
				{
					if(dec_values[pos++] > 0)
						++vote[i];
					else
						++vote[j];
				}

			int vote_max_idx = 0;
			for(i=1;i<nr_class;i++)
				if(vote[i] > vote[vote_max_idx])
					vote_max_idx = i;
			return model.label[vote_max_idx];
		}
	}

	/**
	 * 
	 * @param model
	 * @param x
	 * @param prob_estimates
	 * @return
	 */
	public static double svmPredictProbability(SVMModel model, SVMNode[] x, double[] prob_estimates)
	{
		if ((model.params.svmType == SVMParameter.C_SVC || model.params.svmType == SVMParameter.NU_SVC) &&
			model.probA!=null && model.probB!=null)
		{
			int i;
			int nr_class = model.nrClasses;
			double[] dec_values = new double[nr_class*(nr_class-1)/2];
			svmPredictValues(model, x, dec_values);

			double min_prob=1e-7;
			double[][] pairwise_prob=new double[nr_class][nr_class];
			
			int k=0;
			for(i=0;i<nr_class;i++)
				for(int j=i+1;j<nr_class;j++)
				{
					pairwise_prob[i][j]=Math.min(Math.max(sigmoidPredict(dec_values[k],model.probA[k],model.probB[k]),min_prob),1-min_prob);
					pairwise_prob[j][i]=1-pairwise_prob[i][j];
					k++;
				}
			multiclassProbability(nr_class,pairwise_prob,prob_estimates);

			int prob_max_idx = 0;
			for(i=1;i<nr_class;i++)
				if(prob_estimates[i] > prob_estimates[prob_max_idx])
					prob_max_idx = i;
			return model.label[prob_max_idx];
		}
		else 
			return svmPredict(model, x);
	}

	/** Table with all the SVM problem formulation names. */
	static final String svmTypeTable[] =
	{
		"c_svc","nu_svc","one_class","epsilon_svr","nu_svr",
	};

	/** An array with all the kernel type names. */
	static final String kernelTypeTable[]=
	{
		"linear","polynomial","rbf","sigmoid",
	};

	/**
	 * Outputs a trained model to disk, for use in future classification tasks.
	 * 
	 * @param model_file_name The name of the file storing the model.
	 * @param model The SVM model.
	 * @throws IOException A problem occured while storing the model.
	 */
	public static void svmSaveModel(String model_file_name, SVMModel model) throws IOException
	{
		DataOutputStream fp = new DataOutputStream(new FileOutputStream(model_file_name));

		SVMParameter param = model.params;

		fp.writeBytes("svm_type "+svmTypeTable[param.svmType]+"\n");
		fp.writeBytes("kernel_type "+kernelTypeTable[param.kernelType]+"\n");

		if(param.kernelType == SVMParameter.POLY)
			fp.writeBytes("degree "+param.degree+"\n");

		if(param.kernelType == SVMParameter.POLY ||
		   param.kernelType == SVMParameter.RBF ||
		   param.kernelType == SVMParameter.SIGMOID)
			fp.writeBytes("gamma "+param.gamma+"\n");

		if(param.kernelType == SVMParameter.POLY ||
		   param.kernelType == SVMParameter.SIGMOID)
			fp.writeBytes("coef0 "+param.coef0+"\n");

		int nr_class = model.nrClasses;
		int l = model.totalNrSupportVectors;
		fp.writeBytes("nr_class "+nr_class+"\n");
		fp.writeBytes("total_sv "+l+"\n");
	
		{
			fp.writeBytes("rho");
			for(int i=0;i<nr_class*(nr_class-1)/2;i++)
				fp.writeBytes(" "+model.rho[i]);
			fp.writeBytes("\n");
		}
	
		if(model.label != null)
		{
			fp.writeBytes("label");
			for(int i=0;i<nr_class;i++)
				fp.writeBytes(" "+model.label[i]);
			fp.writeBytes("\n");
		}

		if(model.probA != null) // regression has probA only
		{
			fp.writeBytes("probA");
			for(int i=0;i<nr_class*(nr_class-1)/2;i++)
				fp.writeBytes(" "+model.probA[i]);
			fp.writeBytes("\n");
		}
		if(model.probB != null) 
		{
			fp.writeBytes("probB");
			for(int i=0;i<nr_class*(nr_class-1)/2;i++)
				fp.writeBytes(" "+model.probB[i]);
			fp.writeBytes("\n");
		}

		if(model.numSupportVectors != null)
		{
			fp.writeBytes("nr_sv");
			for(int i=0;i<nr_class;i++)
				fp.writeBytes(" "+model.numSupportVectors[i]);
			fp.writeBytes("\n");
		}

		fp.writeBytes("SV\n");
		double[][] sv_coef = model.supportVectorsCoef;
		SVMNode[][] SV = model.supportVectors;

		for(int i=0;i<l;i++)
		{
			for(int j=0;j<nr_class-1;j++)
				fp.writeBytes(sv_coef[j][i]+" ");

			SVMNode[] p = SV[i];
			for(int j=0;j<p.length;j++)
				fp.writeBytes(p[j].index+":"+p[j].value+" ");
			fp.writeBytes("\n");
		}

		fp.close();
	}

	/**
	 * Loads a trained SVM model from disk.
	 * 
	 * @param model_file_name The path name for the file containing the model.
	 * @return An SVM model. 
	 * @throws IOException A problem occured while reading the model file.
	 */
	public static SVMModel svmLoadModel(String model_file_name) throws IOException
	{
		BufferedReader fp = new BufferedReader(new FileReader(model_file_name));

		// read parameters

		SVMModel model = new SVMModel();
		SVMParameter param = new SVMParameter();
		model.params = param;
		model.rho = null;
		model.probA = null;
		model.probB = null;
		model.label = null;
		model.numSupportVectors = null;

		while(true)
		{
			String cmd = fp.readLine();
			String arg = cmd.substring(cmd.indexOf(' ')+1);

			if(cmd.startsWith("svm_type"))
			{
				int i;
				for(i=0;i<svmTypeTable.length;i++)
				{
					if(arg.indexOf(svmTypeTable[i])!=-1)
					{
						param.svmType=i;
						break;
					}
				}
				if(i == svmTypeTable.length)
				{
					System.err.print("unknown svm type.\n");
					return null;
				}
			}
			else if(cmd.startsWith("kernel_type"))
			{
				int i;
				for(i=0;i<kernelTypeTable.length;i++)
				{
					if(arg.indexOf(kernelTypeTable[i])!=-1)
					{
						param.kernelType=i;
						break;
					}
				}
				if(i == kernelTypeTable.length)
				{
					System.err.print("unknown kernel function.\n");
					return null;
				}
			}
			else if(cmd.startsWith("degree"))
				param.degree = Double.valueOf(arg).doubleValue();
			else if(cmd.startsWith("gamma"))
				param.gamma = Double.valueOf(arg).doubleValue();
			else if(cmd.startsWith("coef0"))
				param.coef0 = Double.valueOf(arg).doubleValue();
			else if(cmd.startsWith("nr_class"))
				model.nrClasses = Integer.parseInt(arg);
			else if(cmd.startsWith("total_sv"))
				model.totalNrSupportVectors = Integer.parseInt(arg);
			else if(cmd.startsWith("rho"))
			{
				int n = model.nrClasses * (model.nrClasses-1)/2;
				model.rho = new double[n];
				StringTokenizer st = new StringTokenizer(arg);
				for(int i=0;i<n;i++)
					model.rho[i] = Double.valueOf(st.nextToken()).doubleValue(); 
			}
			else if(cmd.startsWith("label"))
			{
				int n = model.nrClasses;
				model.label = new int[n];
				StringTokenizer st = new StringTokenizer(arg);
				for(int i=0;i<n;i++)
					model.label[i] = Integer.parseInt(st.nextToken());					
			}
			else if(cmd.startsWith("probA"))
			{
				int n = model.nrClasses*(model.nrClasses-1)/2;
				model.probA = new double[n];
				StringTokenizer st = new StringTokenizer(arg);
				for(int i=0;i<n;i++)
					model.probA[i] = Double.valueOf(st.nextToken()).doubleValue();					
			}
			else if(cmd.startsWith("probB"))
			{
				int n = model.nrClasses*(model.nrClasses-1)/2;
				model.probB = new double[n];
				StringTokenizer st = new StringTokenizer(arg);
				for(int i=0;i<n;i++)
					model.probB[i] = Double.valueOf(st.nextToken()).doubleValue();					
			}
			else if(cmd.startsWith("nr_sv"))
			{
				int n = model.nrClasses;
				model.numSupportVectors = new int[n];
				StringTokenizer st = new StringTokenizer(arg);
				for(int i=0;i<n;i++)
					model.numSupportVectors[i] = Integer.parseInt(st.nextToken());
			}
			else if(cmd.startsWith("SV"))
			{
				break;
			}
			else
			{
				System.err.print("unknown text in model file\n");
				return null;
			}
		}

		// read sv_coef and SV

		int m = model.nrClasses - 1;
		int l = model.totalNrSupportVectors;
		model.supportVectorsCoef = new double[m][l];
		model.supportVectors = new SVMNode[l][];

		for(int i=0;i<l;i++)
		{
			String line = fp.readLine();
			StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");

			for(int k=0;k<m;k++)
				model.supportVectorsCoef[k][i] = Double.valueOf(st.nextToken()).doubleValue();
			int n = st.countTokens()/2;
			model.supportVectors[i] = new SVMNode[n];
			for(int j=0;j<n;j++)
			{
				model.supportVectors[i][j] = new SVMNode();
				model.supportVectors[i][j].index = Integer.parseInt(st.nextToken());
				model.supportVectors[i][j].value = Double.valueOf(st.nextToken()).doubleValue();
			}
		}

		fp.close();
		return model;
	}

	/**
	 * Check the parameters given to an SVM problem.
	 *  
	 * @param prob An SVM Problem.
	 * @param param The parameters.
	 * @return A string with an informative message about the found problems, or an empty String.
	 */
	public static String svmCheckParameter(SVMProblem prob, SVMParameter param)
	{
		// svm_type

		int svm_type = param.svmType;
		if(svm_type != SVMParameter.C_SVC &&
		   svm_type != SVMParameter.NU_SVC &&
		   svm_type != SVMParameter.ONE_CLASS &&
		   svm_type != SVMParameter.EPSILON_SVR &&
		   svm_type != SVMParameter.NU_SVR)
		return "unknown svm type";
	
		// kernel_type
	
		int kernel_type = param.kernelType;
		if(kernel_type != SVMParameter.LINEAR &&
		   kernel_type != SVMParameter.POLY &&
		   kernel_type != SVMParameter.RBF &&
		   kernel_type != SVMParameter.SIGMOID)
		return "unknown kernel type";

		// cache_size,eps,C,nu,p,shrinking

		if(param.cacheSize <= 0)
			return "cache_size <= 0";

		if(param.eps <= 0)
			return "eps <= 0";

		if(svm_type == SVMParameter.C_SVC ||
		   svm_type == SVMParameter.EPSILON_SVR ||
		   svm_type == SVMParameter.NU_SVR)
			if(param.C <= 0)
				return "C <= 0";

		if(svm_type == SVMParameter.NU_SVC ||
		   svm_type == SVMParameter.ONE_CLASS ||
		   svm_type == SVMParameter.NU_SVR)
			if(param.nu < 0 || param.nu > 1)
				return "nu < 0 or nu > 1";

		if(svm_type == SVMParameter.EPSILON_SVR)
			if(param.p < 0)
				return "p < 0";

		if(param.probability &&
		   svm_type == SVMParameter.ONE_CLASS)
			return "one-class SVM probability output not supported yet";
		
		// check whether nu-svc is feasible
	
		if(svm_type == SVMParameter.NU_SVC)
		{
			int l = prob.l;
			int max_nr_class = 16;
			int nr_class = 0;
			int[] label = new int[max_nr_class];
			int[] count = new int[max_nr_class];

			int i;
			for(i=0;i<l;i++)
			{
				int this_label = (int)prob.y[i];
				int j;
				for(j=0;j<nr_class;j++)
					if(this_label == label[j])
					{
						++count[j];
						break;
					}

				if(j == nr_class)
				{
					if(nr_class == max_nr_class)
					{
						max_nr_class *= 2;
						int[] new_data = new int[max_nr_class];
						System.arraycopy(label,0,new_data,0,label.length);
						label = new_data;
						
						new_data = new int[max_nr_class];
						System.arraycopy(count,0,new_data,0,count.length);
						count = new_data;
					}
					label[nr_class] = this_label;
					count[nr_class] = 1;
					++nr_class;
				}
			}

			for(i=0;i<nr_class;i++)
			{
				int n1 = count[i];
				for(int j=i+1;j<nr_class;j++)
				{
					int n2 = count[j];
					if(param.nu*(n1+n2)/2 > Math.min(n1,n2))
						return "specified nu is infeasible";
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param model An SVM model.
	 * @return
	 */
	public static int svmCheckProbabilityModel(SVMModel model)
	{
		if (((model.params.svmType == SVMParameter.C_SVC || model.params.svmType == SVMParameter.NU_SVC) &&
		model.probA!=null && model.probB!=null) ||
		((model.params.svmType == SVMParameter.EPSILON_SVR || model.params.svmType == SVMParameter.NU_SVR) &&
		 model.probA!=null))
			return 1;
		else
			return 0;
	}
}

/**
 * Inner class representing a Kernel matrix for Support Vector classification.
 */
class SVCQ extends Kernel
{
	private final byte[] y;
	private final Cache cache;

	SVCQ(SVMProblem prob, SVMParameter param, byte[] y_)
	{
		super(prob.l, prob.x, param);
		y = (byte[])y_.clone();
		cache = new Cache(prob.l,(int)(param.cacheSize*(1<<20)));
	}

	float[] getQ(int i, int len)
	{
		float[][] data = new float[1][];
		int start;
		if((start = cache.getData(i,data,len)) < len)
		{
			for(int j=start;j<len;j++)
				data[0][j] = (float)(y[i]*y[j]*kernelFunction(i,j));
		}
		return data[0];
	}

	void swapIndex(int i, int j)
	{
		cache.swapIndex(i,j);
		super.swapIndex(i,j);
		do {byte _=y[i]; y[i]=y[j]; y[j]=_;} while(false);
	}
}

/**
 * Inner class representing a Kernel matrix for SVM Distribution Estimation.
 */
class ONECLASSQ extends Kernel
{
	private final Cache cache;

	ONECLASSQ(SVMProblem prob, SVMParameter param)
	{
		super(prob.l, prob.x, param);
		cache = new Cache(prob.l,(int)(param.cacheSize*(1<<20)));
	}

	float[] getQ(int i, int len)
	{
		float[][] data = new float[1][];
		int start;
		if((start = cache.getData(i,data,len)) < len)
		{
			for(int j=start;j<len;j++)
				data[0][j] = (float)kernelFunction(i,j);
		}
		return data[0];
	}

	void swapIndex(int i, int j)
	{
		cache.swapIndex(i,j);
		super.swapIndex(i,j);
	}
}

/**
 * Inner class representing a Kernel matrix for Support Vector regression.
 */
class SVRQ extends Kernel
{
	private final int l;
	private final Cache cache;
	private final byte[] sign;
	private final int[] index;
	private int next_buffer;
	private float[][] buffer;

	SVRQ(SVMProblem prob, SVMParameter param)
	{
		super(prob.l, prob.x, param);
		l = prob.l;
		cache = new Cache(l,(int)(param.cacheSize*(1<<20)));
		sign = new byte[2*l];
		index = new int[2*l];
		for(int k=0;k<l;k++)
		{
			sign[k] = 1;
			sign[k+l] = -1;
			index[k] = k;
			index[k+l] = k;
		}
		buffer = new float[2][2*l];
		next_buffer = 0;
	}

	void swapIndex(int i, int j)
	{
		do {byte _=sign[i]; sign[i]=sign[j]; sign[j]=_;} while(false);
		do {int _=index[i]; index[i]=index[j]; index[j]=_;} while(false);
	}

	float[] getQ(int i, int len)
	{
		float[][] data = new float[1][];
		int real_i = index[i];
		if(cache.getData(real_i,data,l) < l)
		{
			for(int j=0;j<l;j++)
				data[0][j] = (float)kernelFunction(real_i,j);
		}

		// reorder and copy
		float buf[] = buffer[next_buffer];
		next_buffer = 1 - next_buffer;
		byte si = sign[i];
		for(int j=0;j<len;j++)
			buf[j] = si * sign[j] * data[0][index[j]];
		return buf;
	}
}
