package pt.tumba.parser.dvi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.StringTokenizer;
import pt.tumba.parser.*;

/**
 * Description of the Class
 *
 * @author bmartins
 * @created 22 de Agosto de 2002
 */
public class DVI2HTML implements DocFilter {

	File scriptfile;

	String psData = "30 dict begin \n" + "/psxFile outfile (w) file def \n" + "/psxCharBuffer 1 string def \n"
			+ "/psxBuffer 1024 string def \n" + "/psxPrint { psxFile exch writestring } bind def \n"
			+ "/psxPrintNumber { round cvi psxBuffer cvs psxFile exch writestring } bind def \n"
			+ "/psxEPS { (E\t) psxPrint psxPrintNumber (\n) psxPrint } def \n" + "/psxEncodedPrint { \n" + "  { \n"
			+ "    dup 9 eq \n" + "    1 index 10 eq or \n" + "    1 index 37 eq or \n"
			+ "      { psxFile 37 write psxCharBuffer 0 3 -1 roll put psxFile psxCharBuffer writehexstring } \n"
			+ "      { psxFile exch write  } ifelse \n" + "  } forall \n" + "} bind def \n" + "/psxShow { \n"
			+ "	(S\t) psxPrint \n" + "	currentpoint exch \n" + "	psxPrintNumber (\t) psxPrint psxPrintNumber \n"
			+ "	(\t) psxPrint \n" + "	1 index psxEncodedPrint \n" + "	(\t) psxPrint \n"
			+ "	systemdict exch get exec \n" + "	currentpoint exch \n"
			+ "	psxPrintNumber (\t) psxPrint psxPrintNumber \n" + "	(\\n) psxPrint	\n" + "} def \n"
			+ "/show		{ /show psxShow } def \n" + "/kshow		{ /kshow psxShow } def \n"
			+ "/widthshow	{ /widthshow psxShow } def \n" + "/ashow		{ /ashow psxShow } def \n"
			+ "/awidthshow	{ /awidthshow psxShow } def \n" + "/setpagedevice { pop } def \n"
			+ "/setcolor { pop } def \n" + "/settransfer { pop } def \n" + "/setgray { pop } def \n"
			+ "/setgraypattern { pop } def \n" + "/showpage	{ psxFile (P\\n) writestring } def \n";

	String pythonData = "import os, sys, regex, posixpath, posix \n" + "from string import split, atoi, find \n"
			+ "from regsub import gsub, sub \n" + "from urllib import unquote \n" + "class Fragment: \n"
			+ "   def __init__(self, x0, y0, s, x1, y1): \n" + "      self.x0 = x0 \n" + "      self.y0 = y0 \n"
			+ "      self.x1 = x1 \n" + "      self.y1 = y1 \n" + "      self.string = s \n"
			+ "   def averageCharWidth(self): \n" + "      return int((self.x1-self.x0)/len(self.string)) \n"
			+ "   def concat(self, fragment): \n" + "      self.x1 = fragment.x1; \n"
			+ "      self.y1 = fragment.y1; \n" + "      self.string = self.string+fragment.string \n"
			+ "   def __str__(self): \n" + "      return \"(\"+`self.x0`+\", \"+`self.y0`+\", \"+self.string+\")\" \n"
			+ "def ReadPostScriptDataFile(filename, worker): \n" + "   data = open(filename) \n" + "   while 1: \n"
			+ "      input = data.readline() \n" + "      if not input: break; \n" + "      if input[0] == \"P\": \n"
			+ "	 worker.newPage() \n"
			+ "      elif (input[0] == \"S\") and (len(split(input[:-1], '\t')[0:6]) == 6): \n"
			+ "	 [tag, x0, y0, string, x1, y1] = split(input[:-1], '\t')[0:6] \n" + "	 if x1 == \"S\": \n"
			+ "	    while 1: \n" + "	       input = data.readline(); \n" + "	       if input[0] != \"S\": break \n"
			+ "	    [x1, y1] = split(input[:-1], '\t') \n" + "	 string = unquote(string) \n"
			+ "	 if len(string) > 0: \n" + "	    worker.textFragment(Fragment(atoi(x0), atoi(y0), \n"
			+ "					 string, \n" + "					 atoi(x1), atoi(y1))) \n" + "      else: \n"
			+ "	 print \"Bad data line.\" \n" + "   data.close() \n" + "   worker.done() \n"
			+ "def CmpRVItems(x, y): \n" + "   if x[1] < y[1]: return -1 \n" + "   elif x[1] > y[1]: return 1 \n"
			+ "   else: return 0 \n" + "class RandomVariable: \n" + "   def __init__(self): \n"
			+ "      self.histogram = {} \n" + "      self.data = [] \n" + "   def addObservation(self, x): \n"
			+ "      self.data.append(x) \n" + "      if self.histogram.has_key(x): \n"
			+ "	 self.histogram[x] = self.histogram[x]+1 \n" + "      else: \n" + "	 self.histogram[x] = 1 \n"
			+ "   def computeStats(self): \n" + "      self.observations = self.histogram.items() \n"
			+ "      self.observations.sort(CmpRVItems) \n" + "      self.observations.reverse() \n"
			+ "      del self.histogram \n" + "      del self.data \n" + "   def mode(self, n): \n"
			+ "      return self.observations[n][0] \n" + "class FirstPassWorker: \n" + "   def __init__(self): \n"
			+ "      self.fragment = None \n" + "      self.DeltaX = RandomVariable(); \n"
			+ "      self.DeltaY = RandomVariable(); \n" + "      self.CharWidth = RandomVariable(); \n"
			+ "   def done(self): pass \n" + "   def newPage(self): \n" + "      self.fragment = None \n"
			+ "   def textFragment(self, fragment): \n" + "      if self.fragment: \n"
			+ "	 deltaY = fragment.y0-self.fragment.y1; \n" + "	 if deltaY != 0: \n"
			+ "	    self.DeltaY.addObservation(deltaY) \n" + "	 deltaX = fragment.x0-self.fragment.x1 \n"
			+ "	 if deltaX < 0: \n" + "	    self.DeltaX.addObservation(-deltaX) \n"
			+ "      self.CharWidth.addObservation(fragment.averageCharWidth()) \n"
			+ "      self.fragment = fragment \n" + "   def computeThresholds(self): \n"
			+ "      self.CharWidth.computeStats() \n" + "      charWidth = self.CharWidth.mode(0) \n"
			+ "      self.DeltaX.computeStats() \n" + "      n = 0 \n"
			+ "      while self.DeltaX.mode(n) < 5*charWidth: n = n+1 \n" + "      lineWidth = self.DeltaX.mode(n) \n"
			+ "      self.DeltaY.computeStats() \n" + "      n = 0 \n"
			+ "      while abs(self.DeltaY.mode(n)) < charWidth: n = n+1 \n"
			+ "      lineSpacing = abs(self.DeltaY.mode(n)) \n" + "      return charWidth, lineWidth, lineSpacing \n"
			+ "class Line: \n" + "   Header = 1 \n" + "   Footer = 2 \n" + "   PageNo = 3 \n" + "   TOCEntry = 4 \n"
			+ "   BibEntry = 5 \n" + "   Plain = 7 \n" + "   def __init__(self): \n" + "      self.string = None; \n"
			+ "      self.baseline = [] \n" + "      self.x0 = None \n" + "      self.y0 = None \n"
			+ "      self.x1 = None \n" + "      self.y1 = None \n" + "      self.type = None \n"
			+ "   def concat(self, fragment): \n" + "      if self.string: \n"
			+ "	 self.string = self.string+\" \"+fragment.string \n" + "      else: \n"
			+ "	 self.string = fragment.string \n" + "	 self.x0 = fragment.x0 \n" + "	 self.y0 = fragment.y0 \n"
			+ "      self.x1 = fragment.x1 \n" + "      self.y1 = fragment.y1 \n"
			+ "      self.baseline.append(fragment.y0) \n" + "      self.baseline.append(fragment.y1) \n"
			+ "   def computeBestBaseline(self): \n" + "      self.baseline.sort(); \n"
			+ "      self.baseline = self.baseline[len(self.baseline)/2] \n" + "   def length(self): \n"
			+ "      return self.x1-self.x0 \n" + "class SecondPassWorker: \n" + "   def __init__(self, charWidth): \n"
			+ "      self.charWidth = charWidth; \n" + "      self.fragment = None; \n" + "      self.line = Line() \n"
			+ "      self.lines = [] \n" + "      self.pages = [] \n" + "   def done(self): \n"
			+ "      self.newPage() \n" + "      for page in self.pages: \n" + "	 for line in page: \n"
			+ "	    line.computeBestBaseline() \n" + "   def newPage(self): \n" + "      if self.fragment: \n"
			+ "	 self.line.concat(self.fragment) \n" + "	 self.lines.append(self.line) \n"
			+ "	 self.line = Line() \n" + "	 self.fragment = None \n" + "      if self.lines: \n"
			+ "	 self.pages.append(self.lines) \n" + "	 self.lines = [] \n" + "   def textFragment(self, fragment): \n"
			+ "      fragment.string = TranslateChars(fragment.string) \n" + "      if self.fragment is None: \n"
			+ "	 self.fragment = fragment \n"
			+ "      elif self.fragment.x1-fragment.x0 > 2*self.charWidth or abs(fragment.y0-self.fragment.y1) > 2*self.charWidth: \n"
			+ "	 # fragment starts a new line \n" + "	 self.line.concat(self.fragment) \n"
			+ "	 self.lines.append(self.line) \n" + "	 self.line = Line() \n" + "	 self.fragment = fragment \n"
			+ "      else: \n" + "	 avgCharWidth = min([self.fragment.averageCharWidth(), \n"
			+ "			     fragment.averageCharWidth()]) \n"
			+ "	 if fragment.x0-self.fragment.x1 <= 0.3*avgCharWidth: \n" + "	    self.fragment.concat(fragment) \n"
			+ "	 else: \n" + "	    self.line.concat(self.fragment) \n" + "	    self.fragment = fragment \n"
			+ "def TranslateChars(string): \n" + "   string = gsub('\\013', 'ff', string) \n"
			+ "   string = gsub('\\014', 'fi', string) \n" + "   string = gsub('\\015', 'fl', string) \n"
			+ "   string = gsub('\\016', 'ffi', string) \n" + "   string = gsub('\\017', 'ffl', string) \n"
			+ "   string = gsub('\\024', '<=', string) \n" + "   string = gsub('\\025', '>=', string) \n"
			+ "   string = gsub('\\027A', 'AA', string) \n" + "   string = gsub('\\027a', 'aa', string) \n"
			+ "   string = gsub('\\031', 'ss', string) \n" + "   string = gsub('\\032', 'ae', string) \n"
			+ "   string = gsub('\\033', 'oe', string) \n" + "   string = gsub('\\034', 'o', string) \n"
			+ "   string = gsub('\\035', 'AE', string) \n" + "   string = gsub('\\036', 'OE', string) \n"
			+ "   string = gsub('\\037', 'O', string) \n" + "   string = gsub('\\256', 'fi', string) \n"
			+ "   string = gsub('\\257', 'fl', string) \n" + "   string = gsub('\\366', 'fi', string) \n"
			+ "   string = gsub('\\377', 'fl', string) \n" + "   string = gsub('[\\000-\\037]', '?', string) \n"
			+ "   string = gsub('[\\177-\\377]', '?', string) \n" + "   return string \n" + "class Document: \n"
			+ "   tocPattern = regex.compile(\"\\(\\([.:] ?\\)+\\) *[0-9]+ *$\") \n"
			+ "   bibPattern = regex.compile(\"^\\[[A-Za-z0-9\\+]+\\] *[A-Z]\") \n"
			+ "   pageNoPattern = regex.symcomp(\" *\\([^0-9]*\\) *\\(<page>[0-9]+\\) *\1 *$\") \n"
			+ "   headerPatternA = regex.symcomp(\"^ *\\(<page>[0-9]+\\) +CHAPTER +[0-9]+\") \n"
			+ "   headerPatternB = regex.symcomp(\"^ *[0-9]+\\.\\([0-9]+\\.\\)* +\\([A-Za-z]+ +\\)+ *\\(<page>[0-9]+\\) *$\") \n"
			+ "   footerPattern = headerPatternB \n" + "   startParaPattern = regex.compile(\"^ *[A-Z]\") \n"
			+ "   hyphenHeadPattern = regex.compile(\"[a-z]-$\") \n"
			+ "   hyphenTailPattern = regex.compile(\"^\\([^ ]+\\)[ ]*\") \n"
			+ "   def __init__(self, charWidth, lineWidth, lineSpacing): \n" + "      self.charWidth = charWidth \n"
			+ "      self.minLineWidth = 0.8*lineWidth \n" + "      self.maxLineSpacing = 1.1*lineSpacing \n"
			+ "   def markupPage(self, lines): \n" + "      if not lines: return \n"
			+ "      lastIndex = len(lines)-1 \n" + "      for i in range(len(lines)): \n"
			+ "	 self.classifyLine(lines, lines[i], i, lastIndex) \n"
			+ "	 self.markParagraphs(lines, lines[i], i, lastIndex) \n" + "      for i in range(len(lines)): \n"
			+ "	 self.markLineBreaks(lines, lines[i], i, lastIndex) \n" + "      blankLines = []; \n"
			+ "      for i in range(len(lines)): \n" + "	 if len(lines[i].string) == 0: \n"
			+ "	    blankLines.append(i) \n" + "	 else: \n"
			+ "	    self.deHyphenate(lines, lines[i], i, lastIndex) \n" + "      while blankLines: \n"
			+ "	 del lines[blankLines[len(blankLines)-1]] \n" + "	 del blankLines[len(blankLines)-1] \n"
			+ "   def classifyLine(self, lines, line, i, lastIndex): \n"
			+ "      if Document.tocPattern.search(line.string) >= 0: \n" + "	 line.type = Line.TOCEntry \n"
			+ "      elif Document.bibPattern.search(line.string) >= 0: \n" + "	 line.type = Line.BibEntry \n"
			+ "      elif (i == 0 or i == lastIndex) and Document.pageNoPattern.search(line.string) >= 0: \n"
			+ "	 line.type = Line.PageNo \n" + "	 line.pageNo = atoi(Document.pageNoPattern.group('page')) \n"
			+ "      elif i == 0 and Document.headerPatternA.search(line.string) >= 0: \n"
			+ "	 line.type = Line.Header \n" + "	 line.pageNo = atoi(Document.headerPatternA.group('page')) \n"
			+ "      elif i == 0 and Document.headerPatternB.search(line.string) >= 0: \n"
			+ "	 line.type = Line.Header \n" + "	 line.pageNo = atoi(Document.headerPatternB.group('page')) \n"
			+ "      elif i == lastIndex and Document.footerPattern.search(line.string) >= 0: \n"
			+ "	 line.type = Line.Footer() \n" + "	 line.pageNo = atoi(Document.footerPattern.group('page')) \n"
			+ "      else: \n" + "	 line.type = Line.Plain \n"
			+ "   def markParagraphs(self, lines, line, i, lastIndex): \n" + "      if i == 0: \n"
			+ "	 line.newPara = 1 \n" + "      else: \n"
			+ "	 if abs(line.baseline-lines[i-1].baseline) > self.maxLineSpacing or line.type == Line.TOCEntry and lines[i-1].type != Line.TOCEntry or line.type == Line.BibEntry: \n"
			+ "	    line.newPara = 1 \n" + "	 elif i < lastIndex: \n"
			+ "	    if line.x0 > lines[i-1].x0+2*self.charWidth and line.x0 > lines[i+1].x0+2*self.charWidth and lines[i+1].length() == max([lines[i-1].length(), \n"
			+ "					   line.length(), \n"
			+ "					   lines[i+1].length()]) and line.length() >= 0.9*lines[i+1].length() and Document.startParaPattern.search(line.string) >= 0: \n"
			+ "	       line.newPara = 1 \n" + "   def markLineBreaks(self, lines, line, i, lastIndex): \n"
			+ "      if i < lastIndex and line.length() < self.minLineWidth and not hasattr(lines[i+1], 'newPara'): \n"
			+ "	 line.lineBreak = 1 \n" + "   def deHyphenate(self, lines, line, i, lastIndex): \n"
			+ "      if i < lastIndex and not hasattr(lines[i+1], 'newPara') and Document.hyphenHeadPattern.search(line.string) >= 0 and Document.hyphenTailPattern.search(lines[i+1].string) >= 0: \n"
			+ "	 line.string = line.string[:-1]+Document.hyphenTailPattern.group(1) \n"
			+ "	 lines[i+1].string = sub(Document.hyphenTailPattern, '', lines[i+1].string) \n" + "class Formatter: \n"
			+ "   def endPage(self): pass \n" + "   def line(self, l): pass \n" + "   def startParagraph(self): pass \n"
			+ "def HTMLQuote(s): \n" + "   quote_chars = '<>&\"' \n"
			+ "   entities = (\"&lt;\", \"&gt;\", \"&amp;\", \"&quot;\") \n" + "   res = '' \n" + "   for c in s: \n"
			+ "      index = find(quote_chars, c) \n" + "      if index >= 0: \n" + "	 res = res + entities[index] \n"
			+ "      else: \n" + "	 res = res + c \n" + "   return res \n" + "class HTMLFormatter(Formatter): \n"
			+ "   def __init__(self, out): \n" + "      self.out = out \n" + "   def start(self): pass \n"
			+ "   def end(self): pass \n" + "   def endPage(self): \n" + "      self.out.write(\"\\n<hr>\\n\") \n"
			+ "   def line(self, l): \n" + "      if l.type == Line.Header or l.type == Line.Footer: \n"
			+ "	 self.out.write(\"<i>\") \n" + "	 self.out.write(HTMLQuote(l.string)) \n"
			+ "	 self.out.write(\"</i>\") \n" + "      self.out.write(HTMLQuote(l.string)) \n"
			+ "   def startParagraph(self): \n" + "      self.out.write(\"\\n<p>\") \n" + "   def lineBreak(self): \n"
			+ "      self.out.write(\"\\n\") \n" + "   def explicitLineBreak(self): \n"
			+ "      self.out.write(\"<br>\\n\") \n" + "class PlainTextFormatter(Formatter): \n"
			+ "   def __init__(self, out): \n" + "      self.out = out \n" + "   def start(self): pass \n"
			+ "   def end(self): pass \n" + "   def endPage(self): pass \n" + "   def line(self, l): \n"
			+ "      self.out.write(l.string) \n" + "   def startParagraph(self): \n"
			+ "      self.out.write(\"\\n\") \n" + "   def lineBreak(self): \n" + "      self.out.write(\"\\n\") \n"
			+ "   def explicitLineBreak(self): \n" + "      self.out.write(\"\\n\") \n"
			+ "def RenderPage(formatter, page): \n" + "   for line in page: \n"
			+ "      if hasattr(line, 'newPara'): \n" + "	 formatter.startParagraph(); \n"
			+ "      formatter.line(line) \n" + "      if hasattr(line, 'lineBreak'): \n"
			+ "	 formatter.explicitLineBreak() \n" + "      else: \n" + "	 formatter.lineBreak() \n"
			+ "   formatter.endPage() \n" + "def RenderDocument(document, formatter, pages): \n"
			+ "   pageSequence = [] \n" + "   formatter.start() \n" + "   for page in pages: \n"
			+ "      document.markupPage(page) \n" + "      RenderPage(formatter, page) \n" + "   formatter.end() \n"
			+ "def CalculateTextStats(file): \n" + "   pass1 = FirstPassWorker() \n"
			+ "   print \"Computing statistics...\" \n" + "   ReadPostScriptDataFile(file, pass1) \n"
			+ "   return pass1.computeThresholds() \n"
			+ "def FormatDocument(file, charWidth, lineWidth, lineSpacing): \n"
			+ "   pass2 = SecondPassWorker(charWidth) \n" + "   print \"Formatting...\" \n"
			+ "   ReadPostScriptDataFile(file, pass2) \n" + "   return pass2.pages \n"
			+ "def MakeFilename(sourceName, newExt): \n" + "   head, tail = posixpath.split(sourceName) \n"
			+ "   root, ext = posixpath.splitext(tail) \n" + "   return root + newExt \n"
			+ "def PostScriptToPSData(psFilename, datFilename): \n"
			+ "   os.system(\"gs -q -dNODISPLAY -soutfile=%s %s/dvi2html.ps %s quit.ps\" % \n"
			+ "	     (datFilename, \".\", psFilename)) \n" + "def PSDataToText(datFilename, formatter): \n"
			+ "   charWidth, lineWidth, lineSpacing = CalculateTextStats(datFilename) \n"
			+ "   pages = FormatDocument(datFilename, charWidth, lineWidth, lineSpacing) \n"
			+ "   document = Document(charWidth, lineWidth, lineSpacing) \n"
			+ "   RenderDocument(document, formatter, pages) \n" + "def main(): \n" + "   if len(sys.argv) < 3: \n"
			+ "      print \"Usage: dvi2html format input [output]\" \n"
			+ "      print \"  format is either html or plain\" \n" + "   format = sys.argv[1] \n"
			+ "   inputFilename = sys.argv[2]; \n" + "   if regex.search(\"\\.ps$\", inputFilename) >= 0: \n"
			+ "      datFilename = MakeFilename(inputFilename, \".dat\") \n"
			+ "      print \"Running %s\" % inputFilename \n"
			+ "      PostScriptToPSData(inputFilename, datFilename) \n" + "   else: \n"
			+ "      datFilename = inputFilename \n" + "   if len(sys.argv) == 4: \n"
			+ "      outFilename = sys.argv[3] \n" + "   else: \n" + "      if format == 'html': \n"
			+ "	 outFilename = MakeFilename(datFilename, \".html\") \n" + "      elif format == '.txt': \n"
			+ "	 outFilename = MakeFilename(datFilename, \".txt\") \n" + "   outFile = open(outFilename, \"w\") \n"
			+ "   if format == 'html': \n" + "      formatter = HTMLFormatter(outFile) \n"
			+ "   elif format == 'plain': \n" + "      formatter = PlainTextFormatter(outFile) \n" + "   else: \n"
			+ "      print \"Unknown format %s.\" % format \n" + "      sys.exit(1) \n"
			+ "   PSDataToText(datFilename, formatter) \n" + "   posix.unlink(datFilename) \n" + "   outFile.close() \n"
			+ "if __name__ == '__main__': \n" + "   main()";
	{
		boolean errord = false;
		try {
			File f1 = File.createTempFile("dvi2html", "py");
			File f2 = File.createTempFile("dvi2html", "ps");
			try {
				f1.delete();
			} catch (Exception e) {
				errord = true;
			}
			try {
				f2.delete();
			} catch (Exception e) {
				errord = true;
			}
			FileOutputStream pythonw = new FileOutputStream(f1);
			pythonw.write(pythonData.getBytes());
			pythonw.close();
			FileOutputStream psw = new FileOutputStream(f2);
			psw.write(psData.getBytes());
			psw.close();
			scriptfile = f1;
		} catch (Exception e) {
			errord = true;
		}
	}

	/**
	 * Description of the Method
	 *
	 * @exception Throwable
	 *                Description of the Exception
	 */
	protected void finalize() throws Throwable {
		boolean errord = false;
		File f1 = File.createTempFile("dvi2html", "py");
		File f2 = File.createTempFile("dvi2html", "ps");
		try {
			f1.delete();
		} catch (Exception e) {
			errord = true;
		}
		try {
			f2.delete();
		} catch (Exception e) {
			errord = true;
		}
		super.finalize();
	}

	private Random rnd = new Random();
	private int sizeCount = 0;

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public int originalSize() {
		return sizeCount;
	}

	public DVI2HTML() throws Exception {
		try {
			NativeExec.execute("dvips --help");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: dvips");
		}
		try {
			NativeExec.execute("python --help ");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: python");
		}
		try {
			NativeExec.execute("gs --help ");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: ghostscript");
		}

	}

	/**
	 * Description of the Method
	 *
	 * @param b2
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertDVIToHTML(byte[] b2) throws Exception {
		String filename = "" + System.currentTimeMillis() + "." + rnd.nextInt();
		boolean errord = false;
		try {
			sizeCount = b2.length;
			String aux = new String("<html>\n");
			String s2;
			java.io.File file1 = java.io.File.createTempFile(filename, "dvi");
			java.io.File file2 = java.io.File.createTempFile(filename, "html");
			java.io.File file3 = java.io.File.createTempFile(filename, "ps");
			FileOutputStream out = new FileOutputStream(file1);
			out.write(b2);
			out.close();
			NativeExec.execute("dvips " + file1.getAbsolutePath() + " -o " + file3.getAbsolutePath());
			NativeExec.execute("python " + scriptfile.getAbsolutePath() + " html " + file3.getAbsolutePath() + " "
					+ file2.getAbsolutePath());
			BufferedReader input = new BufferedReader(new FileReader(file2));
			while ((s2 = input.readLine()) != null) {
				s2 = s2.trim();
				String s3 = new String();
				StringTokenizer st = new StringTokenizer(s2);
				while (st.hasMoreTokens()) {
					String s = st.nextToken();
					if (s.startsWith("http://")) {
						s = "<a href='" + s + "'>" + s + "</a>;";
					}
					s3 += s;
					s3 += " ";
				}
				aux += s3 + "\n";
			}
			if (aux.equals("<html>\n")) {
				throw new Exception("Empty Content.");
			}
			aux += "</html>";
			input.close();
			file1.delete();
			file2.delete();
			try {
				(java.io.File.createTempFile(filename, "ps")).delete();
			} catch (Exception e2) {
				errord = true;
			}
			try {
				(java.io.File.createTempFile(filename, "dat")).delete();
			} catch (Exception e2) {
				errord = true;
			}
			return aux;
		} catch (Exception e) {
			try {
				(java.io.File.createTempFile(filename, "dvi")).delete();
			} catch (Exception e2) {
				errord = true;
			}
			try {
				(java.io.File.createTempFile(filename, "html")).delete();
			} catch (Exception e2) {
				errord = true;
			}
			try {
				(java.io.File.createTempFile(filename, "ps")).delete();
			} catch (Exception e2) {
				errord = true;
			}
			try {
				(java.io.File.createTempFile(filename, "dat")).delete();
			} catch (Exception e2) {
				errord = true;
			}
			throw (e);
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertDVIToHTML(InputStream input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(input);
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s = -1;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertDVIToHTML(sb.toByteArray());
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertDVIToHTML(java.io.File input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(new FileInputStream(input));
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s = -1;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertDVIToHTML(sb.toByteArray());
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertDVIToHTML(URL input) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) input.openConnection();
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows 98");
		conn.setInstanceFollowRedirects(true);
		conn.connect();
		BufferedInputStream strm = new BufferedInputStream(conn.getInputStream());
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s = -1;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertDVIToHTML(sb.toByteArray());
	}

}
