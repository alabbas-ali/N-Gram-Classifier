
  TCatNG :: Text Categorization With N-Grams


  What is it?
  -----------

  TCatNG is a 100% pure Java library  that you can use to apply N-Gram
  analysis techniques to the process of categorizing text files.
  
  The package includes several different categorization algorithms, namelly
  SVMs, Bayesian Logistic Regression, NN classification and a text compression
  based algorithm. In the case of SVM and Bayesian Logistic Regression, a
  "one-against-one" apprach is used for multiclass classification.  For a more
  detailed description of these learning algorithms and the available options please
  consult the supplied javadocs.

  The Latest Version
  ------------------

  The latest version is available from the TCatNG project web site 
  ( http://tcatng.sourceforge.net/ ).

  Requirements
  ------------

  The following requirements exist for installing and running TCatNG:

   o  Java Interpreter:

      A fully compliant Java Runtime environment is needed for TCatNG to operate.

  Installation Instructions and Documentation
  -------------------------------------------

  Unzip the package and from the command line type:
  
  	java pt.tumba.ngram.TCatNG
  
  The list of command options will appear on screen. For more detailed
  documentation please consult the supplied javadocs.

  Problems and Limitations
  -------------------------------------
  
  This is a list of features we are planning to include on future versions of TCatNG
  
  o N-grams of words and N-grams of characters. The type should be specified at command
  line through the use of regular expressions.
  
  o Bayesian Logistic Regression still does not work. We need to implement the
  multiclass extension.

  Availability, licensing and legal issues
  --------------------------

  TCatNG is released under the BSD License. Source code is included on the package.