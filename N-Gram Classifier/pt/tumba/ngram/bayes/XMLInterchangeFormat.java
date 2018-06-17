package pt.tumba.ngram.bayes;

import java.util.Vector;
import java.util.Enumeration;

/*
 * Definition of the Interchange Format class and its
 * variables. The IFBayesNet ifbn contains the
 * parsed Bayesian network.
 */
public class XMLInterchangeFormat extends InterchangeFormat {

	final int EOF = 0;
	final int SOT = 3;
	final int EOT = 4;
	final int CT = 5;
	final int OPENTAG = 6;
	final int EQUAL = 7;
	final int BIF = 8;
	final int VERSION = 9;
	final int FOR = 10;
	final int GIVEN = 11;
	final int NAME = 12;
	final int NETWORK = 13;
	final int DEFINITION = 14;
	final int PROPERTY = 15;
	final int TABLE = 16;
	final int TYPE = 17;
	final int OUTCOME = 18;
	final int VARIABLE = 19;
	final int NATURE = 20;
	final int DECISION = 21;
	final int UTILITY = 22;
	final int NON_NEGATIVE_NUMBER = 23;
	final int EXPONENT = 24;
	final int IDENTIFIER = 25;
	final int LETTER = 26;
	final int DIGIT = 27;
	final int PCDATA_CHARACTER = 28;
	final int ATTRIBUTE_STRING = 29;
	final int DEFAULT = 0;

	String[] tokenImage = {
	  "<EOF>",
	  "<token of kind 1>",
	  "<token of kind 2>",
	  "\"<\"",
	  "\"</\"",
	  "\">\"",
	  "<OPENTAG>",
	  "\"=\"",
	  "\"BIF\"",
	  "\"version\"",
	  "\"FOR\"",
	  "\"GIVEN\"",
	  "\"NAME\"",
	  "\"NETWORK\"",
	  "\"DEFINITION\"",
	  "\"PROPERTY\"",
	  "\"TABLE\"",
	  "\"TYPE\"",
	  "\"OUTCOME\"",
	  "\"VARIABLE\"",
	  "\"\\\"nature\\\"\"",
	  "\"\\\"decision\\\"\"",
	  "\"\\\"utility\\\"\"",
	  "<NON_NEGATIVE_NUMBER>",
	  "<EXPONENT>",
	  "<IDENTIFIER>",
	  "<LETTER>",
	  "<DIGIT>",
	  "<PCDATA_CHARACTER>",
	  "<ATTRIBUTE_STRING>",
	};


private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
	  case 34:
		 return jjMoveStringLiteralDfa1_0(0x700000L);
	  case 60:
		 jjmatchedKind = 3;
		 return jjMoveStringLiteralDfa1_0(0x10L);
	  case 61:
		 jjmatchedKind = 7;
		 return jjMoveNfa_0(1, 0);
	  case 62:
		 jjmatchedKind = 5;
		 return jjMoveNfa_0(1, 0);
	  case 66:
		 return jjMoveStringLiteralDfa1_0(0x100L);
	  case 68:
		 return jjMoveStringLiteralDfa1_0(0x4000L);
	  case 70:
		 return jjMoveStringLiteralDfa1_0(0x400L);
	  case 71:
		 return jjMoveStringLiteralDfa1_0(0x800L);
	  case 78:
		 return jjMoveStringLiteralDfa1_0(0x3000L);
	  case 79:
		 return jjMoveStringLiteralDfa1_0(0x40000L);
	  case 80:
		 return jjMoveStringLiteralDfa1_0(0x8000L);
	  case 84:
		 return jjMoveStringLiteralDfa1_0(0x30000L);
	  case 86:
		 return jjMoveStringLiteralDfa1_0(0x80200L);
	  case 98:
		 return jjMoveStringLiteralDfa1_0(0x100L);
	  case 100:
		 return jjMoveStringLiteralDfa1_0(0x4000L);
	  case 102:
		 return jjMoveStringLiteralDfa1_0(0x400L);
	  case 103:
		 return jjMoveStringLiteralDfa1_0(0x800L);
	  case 110:
		 return jjMoveStringLiteralDfa1_0(0x3000L);
	  case 111:
		 return jjMoveStringLiteralDfa1_0(0x40000L);
	  case 112:
		 return jjMoveStringLiteralDfa1_0(0x8000L);
	  case 116:
		 return jjMoveStringLiteralDfa1_0(0x30000L);
	  case 118:
		 return jjMoveStringLiteralDfa1_0(0x80200L);
	  default :
		 return jjMoveNfa_0(1, 0);
   }
}
private final int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(1, 0);
   }
   switch(curChar)
   {
	  case 47:
		 if ((active0 & 0x10L) != 0L)
		 {
			jjmatchedKind = 4;
			jjmatchedPos = 1;
		 }
		 break;
	  case 65:
		 return jjMoveStringLiteralDfa2_0(active0, 0x91000L);
	  case 69:
		 return jjMoveStringLiteralDfa2_0(active0, 0x6200L);
	  case 73:
		 return jjMoveStringLiteralDfa2_0(active0, 0x900L);
	  case 79:
		 return jjMoveStringLiteralDfa2_0(active0, 0x400L);
	  case 82:
		 return jjMoveStringLiteralDfa2_0(active0, 0x8000L);
	  case 85:
		 return jjMoveStringLiteralDfa2_0(active0, 0x40000L);
	  case 89:
		 return jjMoveStringLiteralDfa2_0(active0, 0x20000L);
	  case 97:
		 return jjMoveStringLiteralDfa2_0(active0, 0x91000L);
	  case 100:
		 return jjMoveStringLiteralDfa2_0(active0, 0x200000L);
	  case 101:
		 return jjMoveStringLiteralDfa2_0(active0, 0x6200L);
	  case 105:
		 return jjMoveStringLiteralDfa2_0(active0, 0x900L);
	  case 110:
		 return jjMoveStringLiteralDfa2_0(active0, 0x100000L);
	  case 111:
		 return jjMoveStringLiteralDfa2_0(active0, 0x400L);
	  case 114:
		 return jjMoveStringLiteralDfa2_0(active0, 0x8000L);
	  case 117:
		 return jjMoveStringLiteralDfa2_0(active0, 0x440000L);
	  case 121:
		 return jjMoveStringLiteralDfa2_0(active0, 0x20000L);
	  default :
		 break;
   }
   return jjMoveNfa_0(1, 1);
}
private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
	  return jjMoveNfa_0(1, 1);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(1, 1);
   }
   switch(curChar)
   {
	  case 66:
		 return jjMoveStringLiteralDfa3_0(active0, 0x10000L);
	  case 70:
		 if ((active0 & 0x100L) != 0L)
		 {
			jjmatchedKind = 8;
			jjmatchedPos = 2;
		 }
		 return jjMoveStringLiteralDfa3_0(active0, 0x4000L);
	  case 77:
		 return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
	  case 79:
		 return jjMoveStringLiteralDfa3_0(active0, 0x8000L);
	  case 80:
		 return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
	  case 82:
		 if ((active0 & 0x400L) != 0L)
		 {
			jjmatchedKind = 10;
			jjmatchedPos = 2;
		 }
		 return jjMoveStringLiteralDfa3_0(active0, 0x80200L);
	  case 84:
		 return jjMoveStringLiteralDfa3_0(active0, 0x42000L);
	  case 86:
		 return jjMoveStringLiteralDfa3_0(active0, 0x800L);
	  case 97:
		 return jjMoveStringLiteralDfa3_0(active0, 0x100000L);
	  case 98:
		 return jjMoveStringLiteralDfa3_0(active0, 0x10000L);
	  case 101:
		 return jjMoveStringLiteralDfa3_0(active0, 0x200000L);
	  case 102:
		 if ((active0 & 0x100L) != 0L)
		 {
			jjmatchedKind = 8;
			jjmatchedPos = 2;
		 }
		 return jjMoveStringLiteralDfa3_0(active0, 0x4000L);
	  case 109:
		 return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
	  case 111:
		 return jjMoveStringLiteralDfa3_0(active0, 0x8000L);
	  case 112:
		 return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
	  case 114:
		 if ((active0 & 0x400L) != 0L)
		 {
			jjmatchedKind = 10;
			jjmatchedPos = 2;
		 }
		 return jjMoveStringLiteralDfa3_0(active0, 0x80200L);
	  case 116:
		 return jjMoveStringLiteralDfa3_0(active0, 0x442000L);
	  case 118:
		 return jjMoveStringLiteralDfa3_0(active0, 0x800L);
	  default :
		 break;
   }
   return jjMoveNfa_0(1, 2);
}
private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
	  return jjMoveNfa_0(1, 2);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(1, 2);
   }
   switch(curChar)
   {
	  case 67:
		 return jjMoveStringLiteralDfa4_0(active0, 0x40000L);
	  case 69:
		 if ((active0 & 0x1000L) != 0L)
		 {
			jjmatchedKind = 12;
			jjmatchedPos = 3;
		 }
		 else if ((active0 & 0x20000L) != 0L)
		 {
			jjmatchedKind = 17;
			jjmatchedPos = 3;
		 }
		 return jjMoveStringLiteralDfa4_0(active0, 0x800L);
	  case 73:
		 return jjMoveStringLiteralDfa4_0(active0, 0x84000L);
	  case 76:
		 return jjMoveStringLiteralDfa4_0(active0, 0x10000L);
	  case 80:
		 return jjMoveStringLiteralDfa4_0(active0, 0x8000L);
	  case 83:
		 return jjMoveStringLiteralDfa4_0(active0, 0x200L);
	  case 87:
		 return jjMoveStringLiteralDfa4_0(active0, 0x2000L);
	  case 99:
		 return jjMoveStringLiteralDfa4_0(active0, 0x240000L);
	  case 101:
		 if ((active0 & 0x1000L) != 0L)
		 {
			jjmatchedKind = 12;
			jjmatchedPos = 3;
		 }
		 else if ((active0 & 0x20000L) != 0L)
		 {
			jjmatchedKind = 17;
			jjmatchedPos = 3;
		 }
		 return jjMoveStringLiteralDfa4_0(active0, 0x800L);
	  case 105:
		 return jjMoveStringLiteralDfa4_0(active0, 0x484000L);
	  case 108:
		 return jjMoveStringLiteralDfa4_0(active0, 0x10000L);
	  case 112:
		 return jjMoveStringLiteralDfa4_0(active0, 0x8000L);
	  case 115:
		 return jjMoveStringLiteralDfa4_0(active0, 0x200L);
	  case 116:
		 return jjMoveStringLiteralDfa4_0(active0, 0x100000L);
	  case 119:
		 return jjMoveStringLiteralDfa4_0(active0, 0x2000L);
	  default :
		 break;
   }
   return jjMoveNfa_0(1, 3);
}
private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
	  return jjMoveNfa_0(1, 3);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(1, 3);
   }
   switch(curChar)
   {
	  case 65:
		 return jjMoveStringLiteralDfa5_0(active0, 0x80000L);
	  case 69:
		 if ((active0 & 0x10000L) != 0L)
		 {
			jjmatchedKind = 16;
			jjmatchedPos = 4;
		 }
		 return jjMoveStringLiteralDfa5_0(active0, 0x8000L);
	  case 73:
		 return jjMoveStringLiteralDfa5_0(active0, 0x200L);
	  case 78:
		 if ((active0 & 0x800L) != 0L)
		 {
			jjmatchedKind = 11;
			jjmatchedPos = 4;
		 }
		 return jjMoveStringLiteralDfa5_0(active0, 0x4000L);
	  case 79:
		 return jjMoveStringLiteralDfa5_0(active0, 0x42000L);
	  case 97:
		 return jjMoveStringLiteralDfa5_0(active0, 0x80000L);
	  case 101:
		 if ((active0 & 0x10000L) != 0L)
		 {
			jjmatchedKind = 16;
			jjmatchedPos = 4;
		 }
		 return jjMoveStringLiteralDfa5_0(active0, 0x8000L);
	  case 105:
		 return jjMoveStringLiteralDfa5_0(active0, 0x200200L);
	  case 108:
		 return jjMoveStringLiteralDfa5_0(active0, 0x400000L);
	  case 110:
		 if ((active0 & 0x800L) != 0L)
		 {
			jjmatchedKind = 11;
			jjmatchedPos = 4;
		 }
		 return jjMoveStringLiteralDfa5_0(active0, 0x4000L);
	  case 111:
		 return jjMoveStringLiteralDfa5_0(active0, 0x42000L);
	  case 117:
		 return jjMoveStringLiteralDfa5_0(active0, 0x100000L);
	  default :
		 break;
   }
   return jjMoveNfa_0(1, 4);
}
private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
	  return jjMoveNfa_0(1, 4);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(1, 4);
   }
   switch(curChar)
   {
	  case 66:
		 return jjMoveStringLiteralDfa6_0(active0, 0x80000L);
	  case 73:
		 return jjMoveStringLiteralDfa6_0(active0, 0x4000L);
	  case 77:
		 return jjMoveStringLiteralDfa6_0(active0, 0x40000L);
	  case 79:
		 return jjMoveStringLiteralDfa6_0(active0, 0x200L);
	  case 82:
		 return jjMoveStringLiteralDfa6_0(active0, 0xa000L);
	  case 98:
		 return jjMoveStringLiteralDfa6_0(active0, 0x80000L);
	  case 105:
		 return jjMoveStringLiteralDfa6_0(active0, 0x404000L);
	  case 109:
		 return jjMoveStringLiteralDfa6_0(active0, 0x40000L);
	  case 111:
		 return jjMoveStringLiteralDfa6_0(active0, 0x200L);
	  case 114:
		 return jjMoveStringLiteralDfa6_0(active0, 0x10a000L);
	  case 115:
		 return jjMoveStringLiteralDfa6_0(active0, 0x200000L);
	  default :
		 break;
   }
   return jjMoveNfa_0(1, 5);
}
private final int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
	  return jjMoveNfa_0(1, 5);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(1, 5);
   }
   switch(curChar)
   {
	  case 69:
		 if ((active0 & 0x40000L) != 0L)
		 {
			jjmatchedKind = 18;
			jjmatchedPos = 6;
		 }
		 break;
	  case 75:
		 if ((active0 & 0x2000L) != 0L)
		 {
			jjmatchedKind = 13;
			jjmatchedPos = 6;
		 }
		 break;
	  case 76:
		 return jjMoveStringLiteralDfa7_0(active0, 0x80000L);
	  case 78:
		 if ((active0 & 0x200L) != 0L)
		 {
			jjmatchedKind = 9;
			jjmatchedPos = 6;
		 }
		 break;
	  case 84:
		 return jjMoveStringLiteralDfa7_0(active0, 0xc000L);
	  case 101:
		 if ((active0 & 0x40000L) != 0L)
		 {
			jjmatchedKind = 18;
			jjmatchedPos = 6;
		 }
		 return jjMoveStringLiteralDfa7_0(active0, 0x100000L);
	  case 105:
		 return jjMoveStringLiteralDfa7_0(active0, 0x200000L);
	  case 107:
		 if ((active0 & 0x2000L) != 0L)
		 {
			jjmatchedKind = 13;
			jjmatchedPos = 6;
		 }
		 break;
	  case 108:
		 return jjMoveStringLiteralDfa7_0(active0, 0x80000L);
	  case 110:
		 if ((active0 & 0x200L) != 0L)
		 {
			jjmatchedKind = 9;
			jjmatchedPos = 6;
		 }
		 break;
	  case 116:
		 return jjMoveStringLiteralDfa7_0(active0, 0x40c000L);
	  default :
		 break;
   }
   return jjMoveNfa_0(1, 6);
}
private final int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
	  return jjMoveNfa_0(1, 6);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(1, 6);
   }
   switch(curChar)
   {
	  case 34:
		 if ((active0 & 0x100000L) != 0L)
		 {
			jjmatchedKind = 20;
			jjmatchedPos = 7;
		 }
		 break;
	  case 69:
		 if ((active0 & 0x80000L) != 0L)
		 {
			jjmatchedKind = 19;
			jjmatchedPos = 7;
		 }
		 break;
	  case 73:
		 return jjMoveStringLiteralDfa8_0(active0, 0x4000L);
	  case 89:
		 if ((active0 & 0x8000L) != 0L)
		 {
			jjmatchedKind = 15;
			jjmatchedPos = 7;
		 }
		 break;
	  case 101:
		 if ((active0 & 0x80000L) != 0L)
		 {
			jjmatchedKind = 19;
			jjmatchedPos = 7;
		 }
		 break;
	  case 105:
		 return jjMoveStringLiteralDfa8_0(active0, 0x4000L);
	  case 111:
		 return jjMoveStringLiteralDfa8_0(active0, 0x200000L);
	  case 121:
		 if ((active0 & 0x8000L) != 0L)
		 {
			jjmatchedKind = 15;
			jjmatchedPos = 7;
		 }
		 return jjMoveStringLiteralDfa8_0(active0, 0x400000L);
	  default :
		 break;
   }
   return jjMoveNfa_0(1, 7);
}
private final int jjMoveStringLiteralDfa8_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
	  return jjMoveNfa_0(1, 7);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(1, 7);
   }
   switch(curChar)
   {
	  case 34:
		 if ((active0 & 0x400000L) != 0L)
		 {
			jjmatchedKind = 22;
			jjmatchedPos = 8;
		 }
		 break;
	  case 79:
		 return jjMoveStringLiteralDfa9_0(active0, 0x4000L);
	  case 110:
		 return jjMoveStringLiteralDfa9_0(active0, 0x200000L);
	  case 111:
		 return jjMoveStringLiteralDfa9_0(active0, 0x4000L);
	  default :
		 break;
   }
   return jjMoveNfa_0(1, 8);
}
private final int jjMoveStringLiteralDfa9_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
	  return jjMoveNfa_0(1, 8);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(1, 8);
   }
   switch(curChar)
   {
	  case 34:
		 if ((active0 & 0x200000L) != 0L)
		 {
			jjmatchedKind = 21;
			jjmatchedPos = 9;
		 }
		 break;
	  case 78:
		 if ((active0 & 0x4000L) != 0L)
		 {
			jjmatchedKind = 14;
			jjmatchedPos = 9;
		 }
		 break;
	  case 110:
		 if ((active0 & 0x4000L) != 0L)
		 {
			jjmatchedKind = 14;
			jjmatchedPos = 9;
		 }
		 break;
	  default :
		 break;
   }
   return jjMoveNfa_0(1, 9);
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
   0x1ff00000fffffffeL, 0xffffffffffffc000L, 0xffffffffL, 0x600000000000000L
};
static final long[] jjbitVec2 = {
   0x0L, 0x0L, 0x0L, 0xff7fffffff7fffffL
};
static final long[] jjbitVec3 = {
   0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec4 = {
   0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffL, 0x0L
};
static final long[] jjbitVec5 = {
   0xffffffffffffffffL, 0xffffffffffffffffL, 0x0L, 0x0L
};
static final long[] jjbitVec6 = {
   0x3fffffffffffL, 0x0L, 0x0L, 0x0L
};
static final long[] jjbitVec7 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec8 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int strKind = jjmatchedKind;
   int strPos = jjmatchedPos;
   int seenUpto;
   input_stream.backup(seenUpto = curPos + 1);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { throw new Error("Internal Error"); }
   curPos = 0;
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 35;
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
			   case 1:
				  if ((0xefffffffffffffffL & l) != 0L)
				  {
					 if (kind > 28)
						kind = 28;
				  }
				  else if (curChar == 60)
					 jjAddStates(0, 1);
				  if ((0x3ff000000000000L & l) != 0L)
				  {
					 if (kind > 23)
						kind = 23;
					 jjCheckNAddStates(2, 5);
				  }
				  else if ((0x100003600L & l) != 0L)
				  {
					 if (kind > 1)
						kind = 1;
					 jjCheckNAdd(0);
				  }
				  else if (curChar == 34)
					 jjCheckNAdd(12);
				  else if (curChar == 36)
				  {
					 if (kind > 25)
						kind = 25;
					 jjCheckNAdd(9);
				  }
				  else if (curChar == 46)
					 jjCheckNAdd(4);
				  if ((0x3fe000000000000L & l) != 0L)
				  {
					 if (kind > 23)
						kind = 23;
					 jjCheckNAdd(2);
				  }
				  break;
			   case 0:
				  if ((0x100003600L & l) == 0L)
					 break;
				  if (kind > 1)
					 kind = 1;
				  jjCheckNAdd(0);
				  break;
			   case 2:
				  if ((0x3ff000000000000L & l) == 0L)
					 break;
				  if (kind > 23)
					 kind = 23;
				  jjCheckNAdd(2);
				  break;
			   case 3:
				  if (curChar == 46)
					 jjCheckNAdd(4);
				  break;
			   case 4:
				  if ((0x3ff000000000000L & l) == 0L)
					 break;
				  if (kind > 23)
					 kind = 23;
				  jjCheckNAddTwoStates(4, 5);
				  break;
			   case 6:
				  if ((0x280000000000L & l) != 0L)
					 jjCheckNAdd(7);
				  break;
			   case 7:
				  if ((0x3ff000000000000L & l) == 0L)
					 break;
				  if (kind > 23)
					 kind = 23;
				  jjCheckNAdd(7);
				  break;
			   case 8:
				  if (curChar != 36)
					 break;
				  if (kind > 25)
					 kind = 25;
				  jjCheckNAdd(9);
				  break;
			   case 9:
				  if ((0x3ff001000000000L & l) == 0L)
					 break;
				  if (kind > 25)
					 kind = 25;
				  jjCheckNAdd(9);
				  break;
			   case 10:
				  if ((0xefffffffffffffffL & l) != 0L && kind > 28)
					 kind = 28;
				  break;
			   case 11:
				  if (curChar == 34)
					 jjCheckNAdd(12);
				  break;
			   case 12:
				  if ((0xfffffffbffffffffL & l) != 0L)
					 jjCheckNAddTwoStates(12, 13);
				  break;
			   case 13:
				  if (curChar == 34 && kind > 29)
					 kind = 29;
				  break;
			   case 14:
				  if ((0x3ff000000000000L & l) == 0L)
					 break;
				  if (kind > 23)
					 kind = 23;
				  jjCheckNAddStates(2, 5);
				  break;
			   case 15:
				  if ((0x3ff000000000000L & l) != 0L)
					 jjCheckNAddTwoStates(15, 16);
				  break;
			   case 16:
				  if (curChar != 46)
					 break;
				  if (kind > 23)
					 kind = 23;
				  jjCheckNAddTwoStates(17, 18);
				  break;
			   case 17:
				  if ((0x3ff000000000000L & l) == 0L)
					 break;
				  if (kind > 23)
					 kind = 23;
				  jjCheckNAddTwoStates(17, 18);
				  break;
			   case 19:
				  if ((0x280000000000L & l) != 0L)
					 jjCheckNAdd(20);
				  break;
			   case 20:
				  if ((0x3ff000000000000L & l) == 0L)
					 break;
				  if (kind > 23)
					 kind = 23;
				  jjCheckNAdd(20);
				  break;
			   case 21:
				  if ((0x3ff000000000000L & l) == 0L)
					 break;
				  if (kind > 23)
					 kind = 23;
				  jjCheckNAddTwoStates(21, 22);
				  break;
			   case 23:
				  if ((0x280000000000L & l) != 0L)
					 jjCheckNAdd(24);
				  break;
			   case 24:
				  if ((0x3ff000000000000L & l) == 0L)
					 break;
				  if (kind > 23)
					 kind = 23;
				  jjCheckNAdd(24);
				  break;
			   case 25:
				  if (curChar == 60)
					 jjAddStates(0, 1);
				  break;
			   case 26:
				  if (curChar == 33)
					 jjCheckNAddTwoStates(27, 28);
				  break;
			   case 27:
				  if ((0xbfffffffffffffffL & l) != 0L)
					 jjCheckNAddTwoStates(27, 28);
				  break;
			   case 28:
				  if (curChar == 62 && kind > 2)
					 kind = 2;
				  break;
			   case 30:
				  if ((0xbfffffffffffffffL & l) != 0L)
					 jjAddStates(6, 7);
				  break;
			   case 31:
				  if (curChar == 62 && kind > 6)
					 kind = 6;
				  break;
			   case 34:
				  if (curChar == 63)
					 jjstateSet[jjnewStateCnt++] = 33;
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
			   case 1:
				  if (kind > 28)
					 kind = 28;
				  if ((0x7fffffe87fffffeL & l) != 0L)
				  {
					 if (kind > 25)
						kind = 25;
					 jjCheckNAdd(9);
				  }
				  break;
			   case 5:
				  if ((0x2000000020L & l) != 0L)
					 jjAddStates(8, 9);
				  break;
			   case 8:
			   case 9:
				  if ((0x7fffffe87fffffeL & l) == 0L)
					 break;
				  if (kind > 25)
					 kind = 25;
				  jjCheckNAdd(9);
				  break;
			   case 10:
				  if (kind > 28)
					 kind = 28;
				  break;
			   case 12:
				  jjAddStates(10, 11);
				  break;
			   case 18:
				  if ((0x2000000020L & l) != 0L)
					 jjAddStates(12, 13);
				  break;
			   case 22:
				  if ((0x2000000020L & l) != 0L)
					 jjAddStates(14, 15);
				  break;
			   case 27:
				  jjAddStates(16, 17);
				  break;
			   case 29:
				  if ((0x100000001000L & l) != 0L)
					 jjCheckNAddTwoStates(30, 31);
				  break;
			   case 30:
				  jjCheckNAddTwoStates(30, 31);
				  break;
			   case 32:
				  if ((0x200000002000L & l) != 0L)
					 jjstateSet[jjnewStateCnt++] = 29;
				  break;
			   case 33:
				  if ((0x100000001000000L & l) != 0L)
					 jjstateSet[jjnewStateCnt++] = 32;
				  break;
			   default : break;
			}
		 } while(i != startsAt);
	  }
	  else
	  {
		 int hiByte = (int)(curChar >> 8);
		 int i1 = hiByte >> 6;
		 long l1 = 1L << (hiByte & 077);
		 int i2 = (curChar & 0xff) >> 6;
		 long l2 = 1L << (curChar & 077);
		 MatchLoop: do
		 {
			switch(jjstateSet[--i])
			{
			   case 1:
				  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
				  {
					 if (kind > 25)
						kind = 25;
					 jjCheckNAdd(9);
				  }
				  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
				  {
					 if (kind > 28)
						kind = 28;
				  }
				  break;
			   case 8:
			   case 9:
				  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
					 break;
				  if (kind > 25)
					 kind = 25;
				  jjCheckNAdd(9);
				  break;
			   case 10:
				  if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 28)
					 kind = 28;
				  break;
			   case 12:
				  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
					 jjAddStates(10, 11);
				  break;
			   case 27:
				  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
					 jjAddStates(16, 17);
				  break;
			   case 30:
				  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
					 jjAddStates(6, 7);
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
	  if ((i = jjnewStateCnt) == (startsAt = 35 - (jjnewStateCnt = startsAt)))
		 break;
	  try { curChar = input_stream.readChar(); }
	  catch(java.io.IOException e) { break; }
   }
   if (jjmatchedPos > strPos)
	  return curPos;

   int toRet = Math.max(curPos, seenUpto);

   if (curPos < toRet)
	  for (i = toRet - Math.min(curPos, seenUpto); i-- > 0; )
		 try { curChar = input_stream.readChar(); }
		 catch(java.io.IOException e) { throw new Error("Internal Error : Please send a bug report."); }

   if (jjmatchedPos < strPos)
   {
	  jjmatchedKind = strKind;
	  jjmatchedPos = strPos;
   }
   else if (jjmatchedPos == strPos && jjmatchedKind > strKind)
	  jjmatchedKind = strKind;

   return toRet;
}
static final int[] jjnextStates = {
   26, 34, 15, 16, 21, 22, 30, 31, 6, 7, 12, 13, 19, 20, 23, 24, 
   27, 28, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
	  case 0:
		 return ((jjbitVec2[i2] & l2) != 0L);
	  case 48:
		 return ((jjbitVec3[i2] & l2) != 0L);
	  case 49:
		 return ((jjbitVec4[i2] & l2) != 0L);
	  case 51:
		 return ((jjbitVec5[i2] & l2) != 0L);
	  case 61:
		 return ((jjbitVec6[i2] & l2) != 0L);
	  default : 
		 if ((jjbitVec0[i1] & l1) != 0L)
			return true;
		 return false;
   }
}
private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
	  case 0:
		 return ((jjbitVec8[i2] & l2) != 0L);
	  default : 
		 if ((jjbitVec7[i1] & l1) != 0L)
			return true;
		 return false;
   }
}
public static final String[] jjstrLiteralImages = {
"", null, null, "\74", "\74\57", "\76", null, "\75", null, null, null, null, 
null, null, null, null, null, null, null, null, "\42\156\141\164\165\162\145\42", 
"\42\144\145\143\151\163\151\157\156\42", "\42\165\164\151\154\151\164\171\42", null, null, null, null, null, null, null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
static final long[] jjtoToken = {
   0x32fffff9L, 
};
static final long[] jjtoSkip = {
   0x6L, 
};
static final long[] jjtoSpecial = {
   0x4L, 
};
private ASCII_UCodeESC_CharStream input_stream;
private final int[] jjrounds = new int[35];
private final int[] jjstateSet = new int[70];
protected char curChar;

public void ReInit(ASCII_UCodeESC_CharStream stream) {
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}

private final void ReInitRounds() {
   int i;
   jjround = 0x80000001;
   for (i = 35; i-- > 0;)
	  jjrounds[i] = 0x80000000;
}

public void ReInit(ASCII_UCodeESC_CharStream stream, int lexState)
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

public final Token nextToken() {
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

  static final int NATURE_DEFINE = 1;
  static final int DECISION_DEFINE = 2;
  static final int UTILITY_DEFINE = 3;

  public IFBayesNet get_ifbn() { return(ifbn); }

  public void invert_probability_tables() {
       ifbn.invert_probability_tables();
  }

  String pcdata() throws ParseException {
      StringBuffer p = new StringBuffer("");
      Token t;
      while (true) {
                t = getToken(1);
                if ((t.kind == 0) || (t.kind == SOT) || (t.kind == CT) || (t.kind == EOT) ) break;
            else { p.append(t.image); getNextToken(); }
      }
      return(p.toString());
  }

  void glob_heading() throws ParseException {
      Token t;
      while (true) {
                t = getToken(1);
                if (t.kind == 0) break;
                else {
                    if (t.kind == SOT) {
                        getNextToken(); t = getToken(1);
                        if (t.kind == BIF) {
                            getNextToken(); break;
                        }
                        else { getNextToken(); }
                    }
                    else { getNextToken(); }
                }
                getNextToken();
          }
  }

/*
 * THE INTERCHANGE FORMAT GRAMMAR STARTS HERE.
 */

/*
 * Basic parsing function. First looks for a Network Declaration,
 * then looks for an arbitrary number of VariableDeclaration or
 * ProbabilityDeclaration non-terminals. The objects are
 * in the vectors ifbn.pvs and ifbn.upfs.
 */
  final public void CompilationUnit() throws ParseException {
IFProbabilityVariable pv;
IFProbabilityFunction upf;
    OpenTag();
    glob_heading();
    NetworkDeclaration();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SOT:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      jj_consume_token(SOT);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VARIABLE:
        pv = VariableDeclaration();
                                 ifbn.add(pv);
        break;
      case DEFINITION:
        upf = ProbabilityDeclaration();
                                     ifbn.add(upf);
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(EOT);
    jj_consume_token(NETWORK);
    jj_consume_token(CT);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EOT:
      jj_consume_token(EOT);
      jj_consume_token(BIF);
      jj_consume_token(CT);
      break;
    case 0:
      jj_consume_token(0);
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void OpenTag() throws ParseException {
    jj_consume_token(OPENTAG);
  }

/*
 * Detect and initialize the network.
 */
  final public void NetworkDeclaration() throws ParseException {
String s, ss;
Vector properties = new Vector();
double version;
    version = VersionDeclaration();
                                 pcdata();
    jj_consume_token(CT);
    jj_consume_token(SOT);
    jj_consume_token(NETWORK);
    jj_consume_token(CT);
    jj_consume_token(SOT);
    jj_consume_token(NAME);
    s = getIdentifier();
    jj_consume_token(EOT);
    jj_consume_token(NAME);
    jj_consume_token(CT);
    label_2:
    while (true) {
      if (jj_2_1(2)) {
        ;
      } else {
        break label_2;
      }
      ss = Property();
                  properties.addElement(ss);
    }
          ifbn = new IFBayesNet(s, properties);
  }

/*
 * Get the format version.
 */
  final public double VersionDeclaration() throws ParseException {
Token t;
double version = 0;
    jj_consume_token(VERSION);
    jj_consume_token(EQUAL);
    t = jj_consume_token(ATTRIBUTE_STRING);
     version = (Double.valueOf( (t.image).substring(1,t.image.length()-1) )).doubleValue();
     {if (true) return(version);}
    throw new Error("Missing return statement in function");
  }

/*
 * Detect a variable declaration.
 */
  final public IFProbabilityVariable VariableDeclaration() throws ParseException {
String s;
IFProbabilityVariable pv;
int type = NATURE_DEFINE;
    jj_consume_token(VARIABLE);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TYPE:
      type = TypeDeclaration();
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
    jj_consume_token(CT);
    s = ProbabilityVariableName();
    pv = VariableContent(s);
    jj_consume_token(EOT);
    jj_consume_token(VARIABLE);
    jj_consume_token(CT);
          {if (true) return(pv);}
    throw new Error("Missing return statement in function");
  }

  final public String ProbabilityVariableName() throws ParseException {
String s;
    jj_consume_token(SOT);
    jj_consume_token(NAME);
    s = getIdentifier();
    jj_consume_token(EOT);
    jj_consume_token(NAME);
    jj_consume_token(CT);
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

  final public int TypeDeclaration() throws ParseException {
int type;
    jj_consume_token(TYPE);
    jj_consume_token(EQUAL);
    type = ProbabilityVariableType();
      {if (true) return(type);}
    throw new Error("Missing return statement in function");
  }

  final public int ProbabilityVariableType() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NATURE:
      jj_consume_token(NATURE);
               {if (true) return(NATURE_DEFINE);}
      break;
    case DECISION:
      jj_consume_token(DECISION);
                 {if (true) return(DECISION_DEFINE);}
      break;
    case UTILITY:
      jj_consume_token(UTILITY);
                 {if (true) return(UTILITY_DEFINE);}
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public IFProbabilityVariable VariableContent(String name) throws ParseException {
int i;
String s, v, svalues[];
Vector properties = new Vector();
Vector values = new Vector();
Enumeration e;
IFProbabilityVariable pv = new IFProbabilityVariable();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SOT:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_3;
      }
      if (jj_2_2(2)) {
        s = Property();
                   properties.addElement(s);
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SOT:
          v = VariableOutcome();
                          values.addElement(v);
          break;
        default:
          jj_la1[6] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
          pv.set_name(name);
          pv.set_properties(properties);
          svalues = new String[ values.size() ];
          for (e = values.elements(), i = 0; e.hasMoreElements(); i++)
            svalues[i] = (String)(e.nextElement());
          pv.set_values(svalues);
          {if (true) return(pv);}
    throw new Error("Missing return statement in function");
  }

  final public String VariableOutcome() throws ParseException {
String s;
    jj_consume_token(SOT);
    jj_consume_token(OUTCOME);
    s = getIdentifier();
    jj_consume_token(EOT);
    jj_consume_token(OUTCOME);
    jj_consume_token(CT);
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

/*
 * Detect a probability declaration.
 */
  final public IFProbabilityFunction ProbabilityDeclaration() throws ParseException {
String vs[];
IFProbabilityFunction upf = new IFProbabilityFunction();
    jj_consume_token(DEFINITION);
    jj_consume_token(CT);
    ProbabilityContent(upf);
    jj_consume_token(EOT);
    jj_consume_token(DEFINITION);
    jj_consume_token(CT);
          {if (true) return(upf);}
    throw new Error("Missing return statement in function");
  }

  final public void ProbabilityContent(IFProbabilityFunction upf) throws ParseException {
int i, j;
double def[] = null;
double tab[] = null;
String s, vs[];
IFProbabilityEntry entry = null;
Enumeration e;

Vector fors = new Vector();
Vector givens = new Vector();
Vector properties = new Vector();
Vector entries = new Vector();
Vector defaults = new Vector();
Vector tables = new Vector();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SOT:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_4;
      }
      jj_consume_token(SOT);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FOR:
        s = ProbabilityFor();
                         fors.addElement(s);
        break;
      case GIVEN:
        s = ProbabilityGiven();
                           givens.addElement(s);
        break;
      case SOT:
        s = Property();
                   properties.addElement(s);
        break;
      case TABLE:
        tab = ProbabilityTable();
                             tables.addElement(tab);
        break;
      default:
        jj_la1[8] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
          upf.set_properties(properties);
          upf.set_defaults(defaults);
          upf.set_entries(entries);
          upf.set_tables(tables);
          upf.set_conditional_index(fors.size());
          vs = new String[ fors.size() + givens.size() ];
      for (e = fors.elements(), i = 0; e.hasMoreElements(); i++)
        vs[i] = (String)(e.nextElement());
      for (e = givens.elements(), j = i; e.hasMoreElements(); j++)
        vs[j] = (String)(e.nextElement());
          upf.set_variables(vs);
  }

  final public String ProbabilityFor() throws ParseException {
String s;
    jj_consume_token(FOR);
    s = getIdentifier();
    jj_consume_token(EOT);
    jj_consume_token(FOR);
    jj_consume_token(CT);
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

  final public String ProbabilityGiven() throws ParseException {
String s;
    jj_consume_token(GIVEN);
    s = getIdentifier();
    jj_consume_token(EOT);
    jj_consume_token(GIVEN);
    jj_consume_token(CT);
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

  final public double[] ProbabilityTable() throws ParseException {
double d[];
    jj_consume_token(TABLE);
    jj_consume_token(CT);
    d = FloatingPointList();
    jj_consume_token(EOT);
    jj_consume_token(TABLE);
    jj_consume_token(CT);
          {if (true) return(d);}
    throw new Error("Missing return statement in function");
  }

/*
 * Some general purpose non-terminals.
 */

/*
 * Pick a list of non-negative floating numbers.
 */
  final public double[] FloatingPointList() throws ParseException {
int i;
Double d;
double ds[];
Vector d_list = new Vector();
Enumeration e;
    d = FloatingPointNumber();
          d_list.addElement(d);
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NON_NEGATIVE_NUMBER: break;
      default:
        jj_la1[9] = jj_gen;
        break label_5;
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

/*
 * Pick a non-negative floating number; necessary to allow
 * ignored characters and comments to exist in the middle
 * of a FloatingPointList().
 */
  final public Double FloatingPointNumber() throws ParseException {
Token t;
    t = jj_consume_token(NON_NEGATIVE_NUMBER);
          {if (true) return( Double.valueOf(t.image) );}
    throw new Error("Missing return statement in function");
  }

/*
 * Property definition.
 */
  final public String Property() throws ParseException {
String s;
    jj_consume_token(SOT);
    jj_consume_token(PROPERTY);
    s = getString();
    jj_consume_token(EOT);
    jj_consume_token(PROPERTY);
    jj_consume_token(CT);
          {if (true) return(s);}
    throw new Error("Missing return statement in function");
  }

/*
 * Identifier.
 */
  final public String getIdentifier() throws ParseException {
Token t;
    jj_consume_token(CT);
    t = jj_consume_token(IDENTIFIER);
                          {if (true) return(t.image);}
    throw new Error("Missing return statement in function");
  }

/*
 * String.
 */
  final public String getString() throws ParseException {
    jj_consume_token(CT);
           {if (true) return( pcdata() );}
    throw new Error("Missing return statement in function");
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_1();
    jj_save(0, xla);
    return retval;
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_2();
    jj_save(1, xla);
    return retval;
  }

  final private boolean jj_3R_6() {
    if (jj_scan_token(SOT)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(PROPERTY)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_1() {
    if (jj_3R_6()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_2() {
    if (jj_3R_6()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }
 
  ASCII_UCodeESC_CharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  final private int[] jj_la1 = new int[10];
  final private int[] jj_la1_0 = {0x8,0x84000,0x11,0x20000,0x700000,0x8,0x8,0x8,0x10c08,0x800000,};
  final private JJXMLBIFv03Calls[] jj_2_rtns = new JJXMLBIFv03Calls[2];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  public XMLInterchangeFormat(java.io.InputStream stream) {
    jj_input_stream = new ASCII_UCodeESC_CharStream(stream, 1, 1);
	if (ASCII_UCodeESC_CharStream.staticFlag) throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
	input_stream = jj_input_stream;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJXMLBIFv03Calls();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    token = token.next = nextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJXMLBIFv03Calls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
		jj_lastpos = jj_scanpos = jj_scanpos.next = nextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    return (jj_scanpos.kind != kind);
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
	else token = token.next = nextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
	  else t = t.next = nextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
	return (jj_ntk = (token.next=nextToken()).kind);
    else return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Enumeration enumerator = jj_expentries.elements(); enumerator.hasMoreElements();) {
        int[] oldentry = (int[])(enumerator.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[30];
    for (int i = 0; i < 30; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 10; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 30; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
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

  final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
      JJXMLBIFv03Calls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
          }
        }
        p = p.next;
      } while (p != null);
    }
    jj_rescan = false;
  }

  final private void jj_save(int index, int xla) {
    JJXMLBIFv03Calls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJXMLBIFv03Calls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

}

final class JJXMLBIFv03Calls {
  int gen;
  Token first;
  int arg;
  JJXMLBIFv03Calls next;
}
