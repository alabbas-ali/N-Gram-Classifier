package pt.tumba.ngram.bayes;

import java.util.Vector;
import java.util.Enumeration;

/* Definition of the Interchange Format class and its         *
 * variables. The IFBayesNet ifbn contains the *
 * parsed bayesian network.                                   */
public class BayesInterchangeFormat extends InterchangeFormat  {

	final int EOF = 0;
	final int SINGLE_LINE_COMMENT = 7;
	final int FORMAL_COMMENT = 8;
	final int MULTI_LINE_COMMENT = 9;
	final int NETWORK = 10;
	final int VARIABLE = 11;
	final int PROBABILITY = 12;
	final int PROPERTY = 13;
	final int VARIABLETYPE = 14;
	final int DISCRETE = 15;
	final int DEFAULTVALUE = 16;
	final int TABLEVALUES = 17;
	final int NON_NEGATIVE_NUMBER = 18;
	final int EXPONENT = 19;
	final int STRING = 20;

	int DEFAULT = 0;

	String[] tokenImage = {
	  "<EOF>",
	  "\" \"",
	  "\"\\t\"",
	  "\"\\n\"",
	  "\"\\r\"",
	  "\"\\f\"",
	  "\",\"",
	  "<SINGLE_LINE_COMMENT>",
	  "<FORMAL_COMMENT>",
	  "<MULTI_LINE_COMMENT>",
	  "\"network\"",
	  "\"variable\"",
	  "\"probability\"",
	  "\"property\"",
	  "\"type\"",
	  "\"discrete\"",
	  "\"default\"",
	  "\"table\"",
	  "<NON_NEGATIVE_NUMBER>",
	  "<EXPONENT>",
	  "<STRING>",
	  "\"{\"",
	  "\"}\"",
	  "\"[\"",
	  "\"]\"",
	  "\";\"",
	  "\"(\"",
	  "\")\"",
	  "\"|\"",
	};


	private final int jjStopStringLiteralDfa_0(int pos, long active0)
	{
	   switch (pos)
	   {
		  default :
			 return -1;
	   }
	}
	private final int jjStartNfa_0(int pos, long active0)
	{
	   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
	}
	private final int jjStopAtPos(int pos, int kind)
	{
	   jjmatchedKind = kind;
	   jjmatchedPos = pos;
	   return pos + 1;
	}
	private final int jjStartNfaWithStates_0(int pos, int kind, int state)
	{
	   jjmatchedKind = kind;
	   jjmatchedPos = pos;
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) { return pos + 1; }
	   return jjMoveNfa_0(state, pos + 1);
	}
	private final int jjMoveStringLiteralDfa0_0()
	{
	   switch(curChar)
	   {
		  case 40:
			 return jjStopAtPos(0, 26);
		  case 41:
			 return jjStopAtPos(0, 27);
		  case 59:
			 return jjStopAtPos(0, 25);
		  case 91:
			 return jjStopAtPos(0, 23);
		  case 93:
			 return jjStopAtPos(0, 24);
		  case 100:
			 return jjMoveStringLiteralDfa1_0(0x18000L);
		  case 110:
			 return jjMoveStringLiteralDfa1_0(0x400L);
		  case 112:
			 return jjMoveStringLiteralDfa1_0(0x3000L);
		  case 116:
			 return jjMoveStringLiteralDfa1_0(0x24000L);
		  case 118:
			 return jjMoveStringLiteralDfa1_0(0x800L);
		  case 123:
			 return jjStopAtPos(0, 21);
		  case 124:
			 return jjStopAtPos(0, 28);
		  case 125:
			 return jjStopAtPos(0, 22);
		  default :
			 return jjMoveNfa_0(0, 0);
	   }
	}
	private final int jjMoveStringLiteralDfa1_0(long active0)
	{
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(0, active0);
		  return 1;
	   }
	   switch(curChar)
	   {
		  case 97:
			 return jjMoveStringLiteralDfa2_0(active0, 0x20800L);
		  case 101:
			 return jjMoveStringLiteralDfa2_0(active0, 0x10400L);
		  case 105:
			 return jjMoveStringLiteralDfa2_0(active0, 0x8000L);
		  case 114:
			 return jjMoveStringLiteralDfa2_0(active0, 0x3000L);
		  case 121:
			 return jjMoveStringLiteralDfa2_0(active0, 0x4000L);
		  default :
			 break;
	   }
	   return jjStartNfa_0(0, active0);
	}
	private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
	{
	   if (((active0 &= old0)) == 0L)
		  return jjStartNfa_0(0, old0); 
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(1, active0);
		  return 2;
	   }
	   switch(curChar)
	   {
		  case 98:
			 return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
		  case 102:
			 return jjMoveStringLiteralDfa3_0(active0, 0x10000L);
		  case 111:
			 return jjMoveStringLiteralDfa3_0(active0, 0x3000L);
		  case 112:
			 return jjMoveStringLiteralDfa3_0(active0, 0x4000L);
		  case 114:
			 return jjMoveStringLiteralDfa3_0(active0, 0x800L);
		  case 115:
			 return jjMoveStringLiteralDfa3_0(active0, 0x8000L);
		  case 116:
			 return jjMoveStringLiteralDfa3_0(active0, 0x400L);
		  default :
			 break;
	   }
	   return jjStartNfa_0(1, active0);
	}
	private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
	{
	   if (((active0 &= old0)) == 0L)
		  return jjStartNfa_0(1, old0); 
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(2, active0);
		  return 3;
	   }
	   switch(curChar)
	   {
		  case 97:
			 return jjMoveStringLiteralDfa4_0(active0, 0x10000L);
		  case 98:
			 return jjMoveStringLiteralDfa4_0(active0, 0x1000L);
		  case 99:
			 return jjMoveStringLiteralDfa4_0(active0, 0x8000L);
		  case 101:
			 if ((active0 & 0x4000L) != 0L)
				return jjStopAtPos(3, 14);
			 break;
		  case 105:
			 return jjMoveStringLiteralDfa4_0(active0, 0x800L);
		  case 108:
			 return jjMoveStringLiteralDfa4_0(active0, 0x20000L);
		  case 112:
			 return jjMoveStringLiteralDfa4_0(active0, 0x2000L);
		  case 119:
			 return jjMoveStringLiteralDfa4_0(active0, 0x400L);
		  default :
			 break;
	   }
	   return jjStartNfa_0(2, active0);
	}
	private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
	{
	   if (((active0 &= old0)) == 0L)
		  return jjStartNfa_0(2, old0); 
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(3, active0);
		  return 4;
	   }
	   switch(curChar)
	   {
		  case 97:
			 return jjMoveStringLiteralDfa5_0(active0, 0x1800L);
		  case 101:
			 if ((active0 & 0x20000L) != 0L)
				return jjStopAtPos(4, 17);
			 return jjMoveStringLiteralDfa5_0(active0, 0x2000L);
		  case 111:
			 return jjMoveStringLiteralDfa5_0(active0, 0x400L);
		  case 114:
			 return jjMoveStringLiteralDfa5_0(active0, 0x8000L);
		  case 117:
			 return jjMoveStringLiteralDfa5_0(active0, 0x10000L);
		  default :
			 break;
	   }
	   return jjStartNfa_0(3, active0);
	}
	private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
	{
	   if (((active0 &= old0)) == 0L)
		  return jjStartNfa_0(3, old0); 
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(4, active0);
		  return 5;
	   }
	   switch(curChar)
	   {
		  case 98:
			 return jjMoveStringLiteralDfa6_0(active0, 0x1800L);
		  case 101:
			 return jjMoveStringLiteralDfa6_0(active0, 0x8000L);
		  case 108:
			 return jjMoveStringLiteralDfa6_0(active0, 0x10000L);
		  case 114:
			 return jjMoveStringLiteralDfa6_0(active0, 0x2400L);
		  default :
			 break;
	   }
	   return jjStartNfa_0(4, active0);
	}
	private final int jjMoveStringLiteralDfa6_0(long old0, long active0)
	{
	   if (((active0 &= old0)) == 0L)
		  return jjStartNfa_0(4, old0); 
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(5, active0);
		  return 6;
	   }
	   switch(curChar)
	   {
		  case 105:
			 return jjMoveStringLiteralDfa7_0(active0, 0x1000L);
		  case 107:
			 if ((active0 & 0x400L) != 0L)
				return jjStopAtPos(6, 10);
			 break;
		  case 108:
			 return jjMoveStringLiteralDfa7_0(active0, 0x800L);
		  case 116:
			 if ((active0 & 0x10000L) != 0L)
				return jjStopAtPos(6, 16);
			 return jjMoveStringLiteralDfa7_0(active0, 0xa000L);
		  default :
			 break;
	   }
	   return jjStartNfa_0(5, active0);
	}
	private final int jjMoveStringLiteralDfa7_0(long old0, long active0)
	{
	   if (((active0 &= old0)) == 0L)
		  return jjStartNfa_0(5, old0); 
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(6, active0);
		  return 7;
	   }
	   switch(curChar)
	   {
		  case 101:
			 if ((active0 & 0x800L) != 0L)
				return jjStopAtPos(7, 11);
			 else if ((active0 & 0x8000L) != 0L)
				return jjStopAtPos(7, 15);
			 break;
		  case 108:
			 return jjMoveStringLiteralDfa8_0(active0, 0x1000L);
		  case 121:
			 if ((active0 & 0x2000L) != 0L)
				return jjStopAtPos(7, 13);
			 break;
		  default :
			 break;
	   }
	   return jjStartNfa_0(6, active0);
	}
	private final int jjMoveStringLiteralDfa8_0(long old0, long active0)
	{
	   if (((active0 &= old0)) == 0L)
		  return jjStartNfa_0(6, old0); 
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(7, active0);
		  return 8;
	   }
	   switch(curChar)
	   {
		  case 105:
			 return jjMoveStringLiteralDfa9_0(active0, 0x1000L);
		  default :
			 break;
	   }
	   return jjStartNfa_0(7, active0);
	}
	private final int jjMoveStringLiteralDfa9_0(long old0, long active0)
	{
	   if (((active0 &= old0)) == 0L)
		  return jjStartNfa_0(7, old0); 
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(8, active0);
		  return 9;
	   }
	   switch(curChar)
	   {
		  case 116:
			 return jjMoveStringLiteralDfa10_0(active0, 0x1000L);
		  default :
			 break;
	   }
	   return jjStartNfa_0(8, active0);
	}
	private final int jjMoveStringLiteralDfa10_0(long old0, long active0)
	{
	   if (((active0 &= old0)) == 0L)
		  return jjStartNfa_0(8, old0); 
	   try { curChar = input_stream.readChar(); }
	   catch(java.io.IOException e) {
		  jjStopStringLiteralDfa_0(9, active0);
		  return 10;
	   }
	   switch(curChar)
	   {
		  case 121:
			 if ((active0 & 0x1000L) != 0L)
				return jjStopAtPos(10, 12);
			 break;
		  default :
			 break;
	   }
	   return jjStartNfa_0(9, active0);
	}
	private final void jjCheckNAdd(int state)
	{
	   if (jjrounds[state] != jjround)
	   {
		  jjstateSet[jjnewStateCnt++] = state;
		  jjrounds[state] = jjround;
	   }
	}
	private final void jjAddStates(int start, int end)
	{
	   do {
		  jjstateSet[jjnewStateCnt++] = jjnextStates[start];
	   } while (start++ != end);
	}
	private final void jjCheckNAddTwoStates(int state1, int state2)
	{
	   jjCheckNAdd(state1);
	   jjCheckNAdd(state2);
	}
	private final void jjCheckNAddStates(int start, int end)
	{
	   do {
		  jjCheckNAdd(jjnextStates[start]);
	   } while (start++ != end);
	}
	private final void jjCheckNAddStates(int start)
	{
	   jjCheckNAdd(jjnextStates[start]);
	   jjCheckNAdd(jjnextStates[start + 1]);
	}
	static final long[] jjbitVec0 = {
	   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
	};
	private final int jjMoveNfa_0(int startState, int curPos)
	{
	   int[] nextStates;
	   int startsAt = 0;
	   jjnewStateCnt = 46;
	   int i = 1;
	   jjstateSet[0] = startState;
	   int j, kind = 0x7fffffff;
	   for (;;)
	   {
		  if (++jjround == 0x7fffffff)
			 ReInitRounds();
		  if (curChar < 64)
		  {
			 long l = 1L << curChar;
			 MatchLoop: do
			 {
				switch(jjstateSet[--i])
				{
				   case 0:
					  if ((0x3ff000000000000L & l) != 0L)
					  {
						 if (kind > 18)
							kind = 18;
						 jjCheckNAddStates(0, 3);
					  }
					  else if (curChar == 47)
						 jjAddStates(4, 6);
					  else if (curChar == 34)
						 jjCheckNAddStates(7, 9);
					  else if (curChar == 46)
						 jjCheckNAdd(3);
					  if ((0x3fe000000000000L & l) != 0L)
					  {
						 if (kind > 18)
							kind = 18;
						 jjCheckNAdd(1);
					  }
					  break;
				   case 1:
					  if ((0x3ff000000000000L & l) == 0L)
						 break;
					  if (kind > 18)
						 kind = 18;
					  jjCheckNAdd(1);
					  break;
				   case 2:
					  if (curChar == 46)
						 jjCheckNAdd(3);
					  break;
				   case 3:
					  if ((0x3ff000000000000L & l) == 0L)
						 break;
					  if (kind > 18)
						 kind = 18;
					  jjCheckNAddTwoStates(3, 4);
					  break;
				   case 5:
					  if ((0x280000000000L & l) != 0L)
						 jjCheckNAdd(6);
					  break;
				   case 6:
					  if ((0x3ff000000000000L & l) == 0L)
						 break;
					  if (kind > 18)
						 kind = 18;
					  jjCheckNAdd(6);
					  break;
				   case 7:
					  if (curChar == 34)
						 jjCheckNAddStates(7, 9);
					  break;
				   case 8:
					  if ((0xfffffffbffffdbffL & l) != 0L)
						 jjCheckNAddStates(7, 9);
					  break;
				   case 10:
					  if ((0x8400000000L & l) != 0L)
						 jjCheckNAddStates(7, 9);
					  break;
				   case 11:
					  if (curChar == 34 && kind > 20)
						 kind = 20;
					  break;
				   case 12:
					  if ((0xff000000000000L & l) != 0L)
						 jjCheckNAddStates(10, 13);
					  break;
				   case 13:
					  if ((0xff000000000000L & l) != 0L)
						 jjCheckNAddStates(7, 9);
					  break;
				   case 14:
					  if ((0xf000000000000L & l) != 0L)
						 jjstateSet[jjnewStateCnt++] = 15;
					  break;
				   case 15:
					  if ((0xff000000000000L & l) != 0L)
						 jjCheckNAdd(13);
					  break;
				   case 16:
					  if ((0x3ff000000000000L & l) == 0L)
						 break;
					  if (kind > 18)
						 kind = 18;
					  jjCheckNAddStates(0, 3);
					  break;
				   case 17:
					  if ((0x3ff000000000000L & l) != 0L)
						 jjCheckNAddTwoStates(17, 18);
					  break;
				   case 18:
					  if (curChar != 46)
						 break;
					  if (kind > 18)
						 kind = 18;
					  jjCheckNAddTwoStates(19, 20);
					  break;
				   case 19:
					  if ((0x3ff000000000000L & l) == 0L)
						 break;
					  if (kind > 18)
						 kind = 18;
					  jjCheckNAddTwoStates(19, 20);
					  break;
				   case 21:
					  if ((0x280000000000L & l) != 0L)
						 jjCheckNAdd(22);
					  break;
				   case 22:
					  if ((0x3ff000000000000L & l) == 0L)
						 break;
					  if (kind > 18)
						 kind = 18;
					  jjCheckNAdd(22);
					  break;
				   case 23:
					  if ((0x3ff000000000000L & l) == 0L)
						 break;
					  if (kind > 18)
						 kind = 18;
					  jjCheckNAddTwoStates(23, 24);
					  break;
				   case 25:
					  if ((0x280000000000L & l) != 0L)
						 jjCheckNAdd(26);
					  break;
				   case 26:
					  if ((0x3ff000000000000L & l) == 0L)
						 break;
					  if (kind > 18)
						 kind = 18;
					  jjCheckNAdd(26);
					  break;
				   case 27:
					  if (curChar == 47)
						 jjAddStates(4, 6);
					  break;
				   case 28:
					  if (curChar == 47)
						 jjCheckNAddStates(14, 16);
					  break;
				   case 29:
					  if ((0xffffffffffffdbffL & l) != 0L)
						 jjCheckNAddStates(14, 16);
					  break;
				   case 30:
					  if ((0x2400L & l) != 0L && kind > 7)
						 kind = 7;
					  break;
				   case 31:
					  if (curChar == 10 && kind > 7)
						 kind = 7;
					  break;
				   case 32:
					  if (curChar == 13)
						 jjstateSet[jjnewStateCnt++] = 31;
					  break;
				   case 33:
					  if (curChar == 42)
						 jjCheckNAddTwoStates(34, 35);
					  break;
				   case 34:
					  if ((0xfffffbffffffffffL & l) != 0L)
						 jjCheckNAddTwoStates(34, 35);
					  break;
				   case 35:
					  if (curChar == 42)
						 jjCheckNAddStates(17, 19);
					  break;
				   case 36:
					  if ((0xffff7bffffffffffL & l) != 0L)
						 jjCheckNAddTwoStates(37, 35);
					  break;
				   case 37:
					  if ((0xfffffbffffffffffL & l) != 0L)
						 jjCheckNAddTwoStates(37, 35);
					  break;
				   case 38:
					  if (curChar == 47 && kind > 8)
						 kind = 8;
					  break;
				   case 39:
					  if (curChar == 42)
						 jjstateSet[jjnewStateCnt++] = 33;
					  break;
				   case 40:
					  if (curChar == 42)
						 jjCheckNAddTwoStates(41, 42);
					  break;
				   case 41:
					  if ((0xfffffbffffffffffL & l) != 0L)
						 jjCheckNAddTwoStates(41, 42);
					  break;
				   case 42:
					  if (curChar == 42)
						 jjCheckNAddStates(20, 22);
					  break;
				   case 43:
					  if ((0xffff7bffffffffffL & l) != 0L)
						 jjCheckNAddTwoStates(44, 42);
					  break;
				   case 44:
					  if ((0xfffffbffffffffffL & l) != 0L)
						 jjCheckNAddTwoStates(44, 42);
					  break;
				   case 45:
					  if (curChar == 47 && kind > 9)
						 kind = 9;
					  break;
				   default : break;
				}
			 } while(i != startsAt);
		  }
		  else if (curChar < 128)
		  {
			 long l = 1L << (curChar & 077);
			 MatchLoop: do
			 {
				switch(jjstateSet[--i])
				{
				   case 4:
					  if ((0x2000000020L & l) != 0L)
						 jjAddStates(23, 24);
					  break;
				   case 8:
					  if ((0xffffffffefffffffL & l) != 0L)
						 jjCheckNAddStates(7, 9);
					  break;
				   case 9:
					  if (curChar == 92)
						 jjAddStates(25, 27);
					  break;
				   case 10:
					  if ((0x14404410000000L & l) != 0L)
						 jjCheckNAddStates(7, 9);
					  break;
				   case 20:
					  if ((0x2000000020L & l) != 0L)
						 jjAddStates(28, 29);
					  break;
				   case 24:
					  if ((0x2000000020L & l) != 0L)
						 jjAddStates(30, 31);
					  break;
				   case 29:
					  jjAddStates(14, 16);
					  break;
				   case 34:
					  jjCheckNAddTwoStates(34, 35);
					  break;
				   case 36:
				   case 37:
					  jjCheckNAddTwoStates(37, 35);
					  break;
				   case 41:
					  jjCheckNAddTwoStates(41, 42);
					  break;
				   case 43:
				   case 44:
					  jjCheckNAddTwoStates(44, 42);
					  break;
				   default : break;
				}
			 } while(i != startsAt);
		  }
		  else
		  {
			 int i2 = (curChar & 0xff) >> 6;
			 long l2 = 1L << (curChar & 077);
			 MatchLoop: do
			 {
				switch(jjstateSet[--i])
				{
				   case 8:
					  if ((jjbitVec0[i2] & l2) != 0L)
						 jjAddStates(7, 9);
					  break;
				   case 29:
					  if ((jjbitVec0[i2] & l2) != 0L)
						 jjAddStates(14, 16);
					  break;
				   case 34:
					  if ((jjbitVec0[i2] & l2) != 0L)
						 jjCheckNAddTwoStates(34, 35);
					  break;
				   case 36:
				   case 37:
					  if ((jjbitVec0[i2] & l2) != 0L)
						 jjCheckNAddTwoStates(37, 35);
					  break;
				   case 41:
					  if ((jjbitVec0[i2] & l2) != 0L)
						 jjCheckNAddTwoStates(41, 42);
					  break;
				   case 43:
				   case 44:
					  if ((jjbitVec0[i2] & l2) != 0L)
						 jjCheckNAddTwoStates(44, 42);
					  break;
				   default : break;
				}
			 } while(i != startsAt);
		  }
		  if (kind != 0x7fffffff)
		  {
			 jjmatchedKind = kind;
			 jjmatchedPos = curPos;
			 kind = 0x7fffffff;
		  }
		  ++curPos;
		  if ((i = jjnewStateCnt) == (startsAt = 46 - (jjnewStateCnt = startsAt)))
			 return curPos;
		  try { curChar = input_stream.readChar(); }
		  catch(java.io.IOException e) { return curPos; }
	   }
	}
	static final int[] jjnextStates = {
	   17, 18, 23, 24, 28, 39, 40, 8, 9, 11, 8, 9, 13, 11, 29, 30, 
	   32, 35, 36, 38, 42, 43, 45, 5, 6, 10, 12, 14, 21, 22, 25, 26, 
	};
	public static final String[] jjstrLiteralImages = {
	"", null, null, null, null, null, null, null, null, null, 
	"\156\145\164\167\157\162\153", "\166\141\162\151\141\142\154\145", 
	"\160\162\157\142\141\142\151\154\151\164\171", "\160\162\157\160\145\162\164\171", "\164\171\160\145", 
	"\144\151\163\143\162\145\164\145", "\144\145\146\141\165\154\164", "\164\141\142\154\145", null, null, null, 
	"\173", "\175", "\133", "\135", "\73", "\50", "\51", "\174", };
	public static final String[] lexStateNames = {
	   "DEFAULT", 
	};
	static final long[] jjtoToken = {
	   0x1ff7fc01L, 
	};
	static final long[] jjtoSkip = {
	   0x3feL, 
	};
	static final long[] jjtoSpecial = {
	   0x380L, 
	};
	private ASCII_CharStream input_stream;
	private final int[] jjrounds = new int[46];
	private final int[] jjstateSet = new int[92];
	protected char curChar;

	public void ReInit(ASCII_CharStream stream)
	{
	   jjmatchedPos = jjnewStateCnt = 0;
	   curLexState = defaultLexState;
	   input_stream = stream;
	   ReInitRounds();
	}

	private final void ReInitRounds()
	{
	   int i;
	   jjround = 0x80000001;
	   for (i = 46; i-- > 0;)
		  jjrounds[i] = 0x80000000;
	}
	public void ReInit(ASCII_CharStream stream, int lexState)
	{
	   ReInit(stream);
	   SwitchTo(lexState);
	}
	public void SwitchTo(int lexState)
	{
	   if (lexState >= 1 || lexState < 0)
		  throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
	   else
		  curLexState = lexState;
	}

	private final Token jjFillToken()
	{
	   Token t = Token.newToken(jjmatchedKind);
	   t.kind = jjmatchedKind;
	   String im = jjstrLiteralImages[jjmatchedKind];
	   t.image = (im == null) ? input_stream.GetImage() : im;
	   t.beginLine = input_stream.getBeginLine();
	   t.beginColumn = input_stream.getBeginColumn();
	   t.endLine = input_stream.getEndLine();
	   t.endColumn = input_stream.getEndColumn();
	   return t;
	}

	int curLexState = 0;
	int defaultLexState = 0;
	int jjnewStateCnt;
	int jjround;
	int jjmatchedPos;
	int jjmatchedKind;

	public final Token nextToken() 
	{
	  int kind;
	  Token specialToken = null;
	  Token matchedToken;
	  int curPos = 0;

	  EOFLoop :
	  for (;;)
	  {   
	   try   
	   {     
		  curChar = input_stream.BeginToken();
	   }     
	   catch(java.io.IOException e)
	   {        
		  jjmatchedKind = 0;
		  matchedToken = jjFillToken();
		  matchedToken.specialToken = specialToken;
		  return matchedToken;
	   }

	   try { 
		  while (curChar <= 44 && (0x100100003600L & (1L << curChar)) != 0L)
			 curChar = input_stream.BeginToken();
	   }
	   catch (java.io.IOException e1) { continue EOFLoop; }
	   jjmatchedKind = 0x7fffffff;
	   jjmatchedPos = 0;
	   curPos = jjMoveStringLiteralDfa0_0();
	   if (jjmatchedKind != 0x7fffffff)
	   {
		  if (jjmatchedPos + 1 < curPos)
			 input_stream.backup(curPos - jjmatchedPos - 1);
		  if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
		  {
			 matchedToken = jjFillToken();
			 matchedToken.specialToken = specialToken;
			 return matchedToken;
		  }
		  else
		  {
			 if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
			 {
				matchedToken = jjFillToken();
				if (specialToken == null)
				   specialToken = matchedToken;
				else
				{
				   matchedToken.specialToken = specialToken;
				   specialToken = (specialToken.next = matchedToken);
				}
			 }
			 continue EOFLoop;
		  }
	   }
	   int error_line = input_stream.getEndLine();
	   int error_column = input_stream.getEndColumn();
	   String error_after = null;
	   boolean EOFSeen = false;
	   try { input_stream.readChar(); input_stream.backup(1); }
	   catch (java.io.IOException e1) {
		  EOFSeen = true;
		  error_after = curPos <= 1 ? "" : input_stream.GetImage();
		  if (curChar == '\n' || curChar == '\r') {
			 error_line++;
			 error_column = 0;
		  }
		  else
			 error_column++;
	   }
	   if (!EOFSeen) {
		  input_stream.backup(1);
		  error_after = curPos <= 1 ? "" : input_stream.GetImage();
	   }
	   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
	  }
	}


  IFBayesNet ifbn;

  public IFBayesNet get_ifbn() { return(ifbn); }

  /* Method responsible for globbing undefined text in an input file */
  void glob_undefined_text() throws ParseException {
        Token t;
        while (true) {
                t = getToken(1);
                if ((t.kind == 0) ||
                    (t.kind == NETWORK) ||
                    (t.kind == VARIABLE) ||
                    (t.kind == PROBABILITY))
                        break;
                else
                        getNextToken();
          }
  }

/* ========================================================== */
/* THE INTERCHANGE FORMAT GRAMMAR STARTS HERE                 */
/* ========================================================== */

/* Basic parsing function. First looks for a Network Declaration, *
 * then looks for an arbitrary number of VariableDeclaration or   *
 * ProbabilityDeclaration non-terminals. The objects are          *
 * in the vectors ifbn.pvs and ifbn.upfs.                         */
  final public void CompilationUnit() throws ParseException {
IFProbabilityVariable pv;
IFProbabilityFunction upf;
          glob_undefined_text();
    NetworkDeclaration();
          glob_undefined_text();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VARIABLE:
      case PROBABILITY:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VARIABLE:
        pv = VariableDeclaration();
          ifbn.add(pv); glob_undefined_text();
        break;
      case PROBABILITY:
        upf = ProbabilityDeclaration();
          ifbn.add(upf); glob_undefined_text();
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(0);
  }

/* ========================================================== */
/* Detect and initialize the network                          */
/* ========================================================== */
  final public void NetworkDeclaration() throws ParseException {
String s;
Vector properties;
    jj_consume_token(NETWORK);
    s = getString();
    properties = NetworkContent();
          ifbn = new IFBayesNet(s, properties);
  }

/* Fill the network list of properties */
  final public Vector NetworkContent() throws ParseException {
Vector properties = new Vector();
String s;
    jj_consume_token(21);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      s = Property();
          properties.addElement(s);
    }
    jj_consume_token(22);
          {if (true) return(properties);}
    throw new Error("Missing return statement in function");
  }

/* ========================================================== */
/* Detect a variable declaration                              */
/* ========================================================== */
  final public IFProbabilityVariable VariableDeclaration() throws ParseException {
String s;
IFProbabilityVariable pv;
    jj_consume_token(VARIABLE);
    s = ProbabilityVariableName();
    pv = VariableContent(s);
          {if (true) return(pv);}
    throw new Error("Missing return statement in function");
  }

/* Fill a variable list of properties */
  final public IFProbabilityVariable VariableContent(String name) throws ParseException {
String s;
String values[] = null;
Vector properties = new Vector();
IFProbabilityVariable pv = new IFProbabilityVariable();
    jj_consume_token(21);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
      case VARIABLETYPE:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
        s = Property();
          properties.addElement(s);
        break;
      case VARIABLETYPE:
        values = VariableDiscrete();
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(22);
          pv.set_name(name);
          pv.set_properties(properties);
          pv.set_values(values);
          {if (true) return(pv);}
    throw new Error("Missing return statement in function");
  }

/* Fill a variable type discrete */
  final public String[] VariableDiscrete() throws ParseException {
String values[] = null;
    jj_consume_token(VARIABLETYPE);
    jj_consume_token(DISCRETE);
    jj_consume_token(23);
    jj_consume_token(NON_NEGATIVE_NUMBER);
    jj_consume_token(24);
    jj_consume_token(21);
    values = VariableValuesList();
    jj_consume_token(22);
    jj_consume_token(25);
          {if (true) return(values);}
    throw new Error("Missing return statement in function");
  }

/* Get the values of a discrete variable */
  final public String[] VariableValuesList() throws ParseException {
int i;
String value;
String values[] = null;
Vector v = new Vector();
Enumeration e;
    value = ProbabilityVariableValue();
          v.addElement(value);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STRING:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_4;
      }
      value = ProbabilityVariableValue();
          v.addElement(value);
    }
          values = new String[v.size()];
          for (e=v.elements(), i=0; e.hasMoreElements(); i++)
                values[i] = (String)(e.nextElement());
          {if (true) return(values);}
    throw new Error("Missing return statement in function");
  }

/* Pick a single word as a probability variable value */
  final public String ProbabilityVariableValue() throws ParseException {
String s;
    s = getString();
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

/* ========================================================== */
/* Detect a probability declaration                           */
/* ========================================================== */
  final public IFProbabilityFunction ProbabilityDeclaration() throws ParseException {
String vs[];
IFProbabilityFunction upf = new IFProbabilityFunction();
    jj_consume_token(PROBABILITY);
    ProbabilityVariablesList(upf);
    ProbabilityContent(upf);
          {if (true) return(upf);}
    throw new Error("Missing return statement in function");
  }

/* Parse the list of Probability variables */
  final public void ProbabilityVariablesList(IFProbabilityFunction upf) throws ParseException {
int i;
Enumeration e;
String variable_name;
int cond = -1;
String vs[];
Vector v_list = new Vector();
    jj_consume_token(26);
    variable_name = ProbabilityVariableName();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 28:
      cond = ConditionalMark(v_list);
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
          v_list.addElement(variable_name);
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STRING:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_5;
      }
      variable_name = ProbabilityVariableName();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 28:
        cond = ConditionalMark(v_list);
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
          v_list.addElement(variable_name);
    }
    jj_consume_token(27);
          vs = new String[v_list.size()];
          for (e=v_list.elements(), i=0; e.hasMoreElements(); i++)
                vs[i] = (String)(e.nextElement());
          upf.set_variables(vs);
          if (cond == -1)
            cond = 1;
          upf.set_conditional_index(cond);
  }

/* Find the conditional mark */
  final public int ConditionalMark(Vector v) throws ParseException {
    jj_consume_token(28);
      {if (true) return(v.size());}
    throw new Error("Missing return statement in function");
  }

/* Pick a single word as a probability variable name */
  final public String ProbabilityVariableName() throws ParseException {
String s;
    s = getString();
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

/* Fill a Probability list of properties */
  final public void ProbabilityContent(IFProbabilityFunction upf) throws ParseException {
String s = null;
Vector properties = new Vector();
IFProbabilityEntry e = null;
Vector entries = new Vector();
Vector defs = new Vector();
Vector tabs = new Vector();
double def[] = null;
double tab[] = null;
    jj_consume_token(21);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
      case DEFAULTVALUE:
      case TABLEVALUES:
      case 26:
        ;
        break;
      default:
        jj_la1[9] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTY:
        s = Property();
          properties.addElement(s);
        break;
      case DEFAULTVALUE:
        def = ProbabilityDefaultEntry();
      defs.addElement(def);
        break;
      case 26:
        e = ProbabilityEntry();
          entries.addElement(e);
        break;
      case TABLEVALUES:
        tab = ProbabilityTable();
      tabs.addElement(tab);
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(22);
          upf.set_properties(properties);
          upf.set_defaults(defs);
          upf.set_entries(entries);
          upf.set_tables(tabs);
  }

  final public IFProbabilityEntry ProbabilityEntry() throws ParseException {
String s[];
double d[];
    s = ProbabilityValuesList();
    d = FloatingPointList();
    jj_consume_token(25);
          {if (true) return( new IFProbabilityEntry(s, d) );}
    throw new Error("Missing return statement in function");
  }

/* Parse the list of Probability values in an entry */
  final public String[] ProbabilityValuesList() throws ParseException {
int i;
Enumeration e;
String variable_name;
String vs[];
Vector v_list = new Vector();
    jj_consume_token(26);
    variable_name = ProbabilityVariableValue();
          v_list.addElement(variable_name);
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STRING:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_7;
      }
      variable_name = ProbabilityVariableValue();
          v_list.addElement(variable_name);
    }
    jj_consume_token(27);
          vs = new String[v_list.size()];
          for (e=v_list.elements(), i=0; e.hasMoreElements(); i++)
                vs[i] = (String)(e.nextElement());
          {if (true) return(vs);}
    throw new Error("Missing return statement in function");
  }

  final public double[] ProbabilityDefaultEntry() throws ParseException {
double d[];
    jj_consume_token(DEFAULTVALUE);
    d = FloatingPointList();
    jj_consume_token(25);
          {if (true) return(d);}
    throw new Error("Missing return statement in function");
  }

  final public double[] ProbabilityTable() throws ParseException {
double d[];
    jj_consume_token(TABLEVALUES);
    d = FloatingPointList();
    jj_consume_token(25);
          {if (true) return(d);}
    throw new Error("Missing return statement in function");
  }

/* ====================================================== */
/*          Some general purpose non-terminals            */
/* ====================================================== */

/* Pick a list of non-negative floating numbers */
  final public double[] FloatingPointList() throws ParseException {
int i;
Double d;
double ds[];
Vector d_list = new Vector();
Enumeration e;
    d = FloatingPointNumber();
          d_list.addElement(d);
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NON_NEGATIVE_NUMBER:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_8;
      }
      d = FloatingPointNumber();
          d_list.addElement(d);
    }
          ds = new double[d_list.size()];
          for (e=d_list.elements(), i=0; e.hasMoreElements(); i++) {
                d = (Double)(e.nextElement());
                ds[i] = d.doubleValue();
          }
          {if (true) return(ds);}
    throw new Error("Missing return statement in function");
  }

/* Pick a non-negative floating number; necessary to allow *
 * ignored characters and comments to exist in the middle  *
 * of a FloatingPointList()                                */
  final public Double FloatingPointNumber() throws ParseException {
Token t;
    t = jj_consume_token(NON_NEGATIVE_NUMBER);
          {if (true) return( Double.valueOf(t.image) );}
    throw new Error("Missing return statement in function");
  }

/* Property definition */
  final public String Property() throws ParseException {
String s;
    jj_consume_token(PROPERTY);
    s = getString();
    jj_consume_token(25);
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

/* String */
  final public String getString() throws ParseException {
Token t;
    t = jj_consume_token(STRING);
          {if (true) return( (t.image).substring(1,t.image.length()-1) );}
    throw new Error("Missing return statement in function");
  }

  ASCII_CharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[13];
  final private int[] jj_la1_0 = {0x1800,0x1800,0x2000,0x6000,0x6000,0x100000,0x10000000,0x100000,0x10000000,0x4032000,0x4032000,0x100000,0x40000,};

  public BayesInterchangeFormat(java.io.InputStream stream) {
	jj_input_stream = new ASCII_CharStream(stream, 1, 1);
	input_stream = jj_input_stream;
	token = new Token();
	jj_ntk = -1;
	jj_gen = 0;
	for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = nextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = nextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = nextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=nextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[29];
    for (int i = 0; i < 29; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 13; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 29; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
