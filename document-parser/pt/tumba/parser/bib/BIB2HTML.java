package pt.tumba.parser.bib;

import java.net.*;
import java.io.*;
import java.util.*;
import pt.tumba.parser.*;

/**
 * Description of the Class
 *
 * @author bmartins
 * @created 22 de Agosto de 2002
 */
public class BIB2HTML implements DocFilter {

	File scriptfile;

	String bibData = "ENTRY" + "  { abstract" + "    address" + "    author" + "    booktitle" + "    chapter"
			+ "    comment" + "    earlier" + "    edition" + "    editor" + "    howpublished" + "    institution"
			+ "    journal" + "    key" + "    keyword" + "    later" + "    month" + "    note" + "    number"
			+ "    organization" + "    pages" + "    private" + "    publisher" + "    school" + "    series"
			+ "    title" + "    type" + "    URL" + "    volume" + "    year" + "  }" + "  {}"
			+ "  { label extra.label sort.label }" + ""
			+ "INTEGERS { output.state before.all mid.sentence after.sentence after.block }" + ""
			+ "FUNCTION {init.state.consts}" + "{ #0 'before.all :=" + "  #1 'mid.sentence :="
			+ "  #2 'after.sentence :=" + "  #3 'after.block :=" + "}" + "" + "STRINGS { s t }" + ""
			+ "FUNCTION {output.nonnull}" + "{ 's :=" + "  output.state mid.sentence =" + "    { \", \" * write$ }"
			+ "    { output.state after.block =" + "	{ add.period$ write$" + "	  newline$" + "	}"
			+ "	{ output.state before.all =" + "	     'write$" + "	     { add.period$ \" \" * write$ }"
			+ "	    if$" + "	}" + "      if$" + "      mid.sentence 'output.state :=" + "    }" + "  if$" + "  s"
			+ "}" + "" + "FUNCTION {output}" + "{ duplicate$ empty$" + "    'pop$" + "    'output.nonnull" + "  if$"
			+ "}" + "" + "FUNCTION {output.check}" + "{ 't :=" + "  duplicate$ empty$"
			+ "    { pop$ \"empty \" t * \" in \" * cite$ * warning$ }" + "    'output.nonnull" + "  if$" + "}" + ""
			+ "% DFK changed to use cite$ for the label" + "% DFK changed to HTML, and use URL to make cite key a link"
			+ "FUNCTION {output.bibitem}" + "{ newline$" + "  URL empty$"
			+ "    { \"<DT> <A NAME=\" quote$ * cite$ * quote$ * " + "	\"> \" * cite$ * \":</A> </DT>\" * write$"
			+ "    }" + "    { \"<DT><A NAME=\" quote$ * cite$ * quote$ * \" HREF=\" * quote$ * "
			+ "	URL * quote$ * \"> \" * cite$ * \":</A></DT>\" * write$" + "    }" + "  if$" + "  newline$"
			+ "  \"<DD>\" write$ newline$" + "  \"\"" + "  before.all 'output.state :=" + "}" + ""
			+ "% DFK changed to HTML" + "FUNCTION {fin.entry}" + "{ add.period$" + "  write$" + "  newline$"
			+ "  earlier empty$" + "	'skip$" + "	{ \"<br>\" write$ newline$"
			+ "	  \"See also earlier version <A HREF=\" quote$ * \"#\" * earlier * quote$ *"
			+ "		\">\" * earlier * \"</A>.\" * write$ newline$" + "	}" + "  if$" + "  later empty$" + "	'skip$"
			+ "	{ \"<br>\" write$ newline$" + "	  \"See also later version <A HREF=\" quote$ * \"#\" * later * quote$ *"
			+ "		\">\" * later * \"</A>.\" * write$ newline$" + "	}" + "  if$" + "  \"</DD>\" write$ newline$"
			+ "}" + "" + "FUNCTION {new.block}" + "{ output.state before.all =" + "    'skip$"
			+ "    { after.block 'output.state := }" + "  if$" + "}" + "" + "FUNCTION {new.sentence}"
			+ "{ output.state after.block =" + "    'skip$" + "    { output.state before.all =" + "	'skip$"
			+ "	{ after.sentence 'output.state := }" + "      if$" + "    }" + "  if$" + "}" + "" + "FUNCTION {not}"
			+ "{   { #0 }" + "    { #1 }" + "  if$" + "}" + "" + "FUNCTION {and}" + "{   'skip$" + "    { pop$ #0 }"
			+ "  if$" + "}" + "" + "FUNCTION {or}" + "{   { pop$ #1 }" + "    'skip$" + "  if$" + "}" + ""
			+ "FUNCTION {new.block.checka}" + "{ empty$" + "    'skip$" + "    'new.block" + "  if$" + "}" + ""
			+ "FUNCTION {new.block.checkb}" + "{ empty$" + "  swap$ empty$" + "  and" + "    'skip$" + "    'new.block"
			+ "  if$" + "}" + "" + "FUNCTION {new.sentence.checka}" + "{ empty$" + "    'skip$" + "    'new.sentence"
			+ "  if$" + "}" + "" + "FUNCTION {new.sentence.checkb}" + "{ empty$" + "  swap$ empty$" + "  and"
			+ "    'skip$" + "    'new.sentence" + "  if$" + "}" + "" + "FUNCTION {field.or.null}"
			+ "{ duplicate$ empty$" + "    { pop$ \"\" }" + "    'skip$" + "  if$" + "}" + "" + "% DFK changed to HTML"
			+ "FUNCTION {emphasize}" + "{ duplicate$ empty$" + "    { pop$ \"\" }"
			+ "    { \"<EM>\" swap$ * \"</EM>\" * }" + "  if$" + "}" + "" + "% DFK added for HTML strong emphasis"
			+ "FUNCTION {strong}" + "{ duplicate$ empty$" + "    { pop$ \"\" }"
			+ "    { \"<STRONG>\" swap$ * \"</STRONG>\" * }" + "  if$" + "}" + ""
			+ "INTEGERS { nameptr namesleft numnames }" + ""
			+ "% DFK added this, to strip {} and ~ from titles and authors"
			+ "% It's not a great idea, because it will screw up in math mode and some"
			+ "% special characters... but it makes most things much prettier." + "FUNCTION {author.title.purify}"
			+ "{ 't :=" + "  \"\"" + "    { t empty$ not }" + "    { t #1 #1 substring$ \"{\" = "
			+ "      t #1 #1 substring$ \"}\" = or" + "      	'skip$" + "	{ t #1 #1 substring$ \"~\" ="
			+ "    	    { \" \" * }" + "    	    { t #1 #1 substring$ * }" + "    	if$" + "    	}" + "      if$"
			+ "      t #2 global.max$ substring$ 't :=" + "    }" + "  while$" + "}" + "" + "FUNCTION {format.names}"
			+ "{ 's :=" + "  #1 'nameptr :=" + "  s num.names$ 'numnames :=" + "  numnames 'namesleft :="
			+ "    { namesleft #0 > }" + "    { s nameptr \"{ff~}{vv~}{ll}{, jj}\" format.name$ 't :="
			+ "      nameptr #1 >" + "	{ namesleft #1 >" + "	    { \", \" * t * }" + "	    { numnames #2 >"
			+ "		{ \",\" * }" + "		'skip$" + "	      if$" + "	      t \"others\" ="
			+ "		{ \" et&nbsp;al.\" * }" + "		{ \" and \" * t * }" + "	      if$" + "	    }" + "	  if$"
			+ "	}" + "	't" + "      if$" + "      nameptr #1 + 'nameptr :=" + "      namesleft #1 - 'namesleft :="
			+ "    }" + "  while$" + "}" + "" + "FUNCTION {format.authors}" + "{ author empty$" + "    { \"\" }"
			+ "    { author format.names  author.title.purify }" + "  if$" + "}" + "" + "FUNCTION {format.editors}"
			+ "{ editor empty$" + "    { \"\" }" + "    { editor format.names" + "      editor num.names$ #1 >"
			+ "	{ \", editors\" * }" + "	{ \", editor\" * }" + "      if$" + "    }" + "  if$" + "}" + ""
			+ "% DFK added strong, so it will be bold." + "FUNCTION {format.title}" + "{ title empty$" + "    { \"\" }"
			+ "    { title \"t\" change.case$  author.title.purify strong }" + "  if$" + "}" + ""
			+ "FUNCTION {n.dashify}" + "{ 't :=" + "  \"\"" + "    { t empty$ not }"
			+ "    { t #1 #1 substring$ \"-\" =" + "	{ t #1 #2 substring$ \"--\" = not" + "	    { \"--\" *"
			+ "	      t #2 global.max$ substring$ 't :=" + "	    }" + "	    {   { t #1 #1 substring$ \"-\" = }"
			+ "		{ \"-\" *" + "		  t #2 global.max$ substring$ 't :=" + "		}" + "	      while$"
			+ "	    }" + "	  if$" + "	}" + "	{ t #1 #1 substring$ *" + "	  t #2 global.max$ substring$ 't :="
			+ "	}" + "      if$" + "    }" + "  while$" + "}" + "" + "FUNCTION {format.date}" + "{ year empty$"
			+ "    { month empty$" + "	{ \"\" }" + "	{ \"there's a month but no year in \" cite$ * warning$"
			+ "	  month" + "	}" + "      if$" + "    }" + "    { month empty$" + "	'year"
			+ "	{ month \" \" * year * }" + "      if$" + "    }" + "  if$" + "}" + ""
			+ "% DFK changed emphasize to strong" + "FUNCTION {format.btitle}" + "{ title author.title.purify strong"
			+ "}" + "" + "FUNCTION {tie.or.space.connect}" + "{ duplicate$ text.length$ #3 <" + "    { \"&nbsp;\" }"
			+ "    { \" \" }" + "  if$" + "  swap$ * *" + "}" + "" + "FUNCTION {either.or.check}" + "{ empty$"
			+ "    'pop$" + "    { \"can't use both \" swap$ * \" fields in \" * cite$ * warning$ }" + "  if$" + "}"
			+ "" + "FUNCTION {format.bvolume}" + "{ volume empty$" + "    { \"\" }"
			+ "    { \"volume\" volume tie.or.space.connect" + "      series empty$" + "	'skip$"
			+ "	{ \" of \" * series emphasize * }" + "      if$" + "      \"volume and number\" number either.or.check"
			+ "    }" + "  if$" + "}" + "" + "FUNCTION {format.number.series}" + "{ volume empty$"
			+ "    { number empty$" + "	{ series field.or.null }" + "	{ output.state mid.sentence ="
			+ "	    { \"number\" }" + "	    { \"Number\" }" + "	  if$" + "	  number tie.or.space.connect"
			+ "	  series empty$" + "	    { \"there's a number but no series in \" cite$ * warning$ }"
			+ "	    { \" in \" * series * }" + "	  if$" + "	}" + "      if$" + "    }" + "    { \"\" }" + "  if$"
			+ "}" + "" + "FUNCTION {format.edition}" + "{ edition empty$" + "    { \"\" }"
			+ "    { output.state mid.sentence =" + "	{ edition \"l\" change.case$ \" edition\" * }"
			+ "	{ edition \"t\" change.case$ \" edition\" * }" + "      if$" + "    }" + "  if$" + "}" + ""
			+ "INTEGERS { multiresult }" + "" + "FUNCTION {multi.page.check}" + "{ 't :=" + "  #0 'multiresult :="
			+ "    { multiresult not" + "      t empty$ not" + "      and" + "    }" + "    { t #1 #1 substring$"
			+ "      duplicate$ \"-\" =" + "      swap$ duplicate$ \",\" =" + "      swap$ \"+\" =" + "      or or"
			+ "	{ #1 'multiresult := }" + "	{ t #2 global.max$ substring$ 't := }" + "      if$" + "    }" + "  while$"
			+ "  multiresult" + "}" + "" + "FUNCTION {format.pages}" + "{ pages empty$" + "    { \"\" }"
			+ "    { pages multi.page.check" + "	{ \"pages\" pages n.dashify tie.or.space.connect }"
			+ "	{ \"page\" pages tie.or.space.connect }" + "      if$" + "    }" + "  if$" + "}" + ""
			+ "FUNCTION {format.vol.num.pages}" + "{ volume field.or.null" + "  number empty$" + "    'skip$"
			+ "    { \"(\" number * \")\" * *" + "      volume empty$"
			+ "	{ \"there's a number but no volume in \" cite$ * warning$ }" + "	'skip$" + "      if$" + "    }"
			+ "  if$" + "  pages empty$" + "    'skip$" + "    { duplicate$ empty$" + "	{ pop$ format.pages }"
			+ "	{ \":\" * pages n.dashify * }" + "      if$" + "    }" + "  if$" + "}" + ""
			+ "FUNCTION {format.chapter.pages}" + "{ chapter empty$" + "    'format.pages" + "    { type empty$"
			+ "	{ \"chapter\" }" + "	{ type \"l\" change.case$ }" + "      if$"
			+ "      chapter tie.or.space.connect" + "      pages empty$" + "	'skip$" + "	{ \", \" * format.pages * }"
			+ "      if$" + "    }" + "  if$" + "}" + "" + "FUNCTION {format.in.ed.booktitle}" + "{ booktitle empty$"
			+ "    { \"\" }" + "    { editor empty$" + "	{ \"In \" booktitle emphasize * }"
			+ "	{ \"In \" format.editors * \", \" * booktitle emphasize * }" + "      if$" + "    }" + "  if$" + "}"
			+ "" + "FUNCTION {empty.misc.check}" + "{ author empty$ title empty$ howpublished empty$"
			+ "  month empty$ year empty$ note empty$" + "  and and and and and" + "  key empty$ not and"
			+ "    { \"all relevant fields are empty in \" cite$ * warning$ }" + "    'skip$" + "  if$" + "}" + ""
			+ "FUNCTION {format.thesis.type}" + "{ type empty$" + "    'skip$" + "    { pop$"
			+ "      type \"t\" change.case$" + "    }" + "  if$" + "}" + "" + "FUNCTION {format.tr.number}"
			+ "{ type empty$" + "    { \"Technical Report\" }" + "    'type" + "  if$" + "  number empty$"
			+ "    { \"t\" change.case$ }" + "    { number tie.or.space.connect }" + "  if$" + "}" + ""
			+ "FUNCTION {format.article.crossref}" + "{ key empty$" + "    { journal empty$"
			+ "	{ \"need key or journal for \" cite$ * \" to crossref \" * crossref *" + "	  warning$" + "	  \"\""
			+ "	}" + "	{ \"In {\\em \" journal * \"\\/}\" * }" + "      if$" + "    }" + "    { \"In \" key * }"
			+ "  if$" + "  \" \\cite{\" * crossref * \"}\" *" + "}" + "" + "FUNCTION {format.crossref.editor}"
			+ "{ editor #1 \"{vv~}{ll}\" format.name$" + "  editor num.names$ duplicate$" + "  #2 >"
			+ "    { pop$ \" et&nbsp;al.\" * }" + "    { #2 <" + "	'skip$"
			+ "	{ editor #2 \"{ff }{vv }{ll}{ jj}\" format.name$ \"others\" =" + "	    { \" et&nbsp;al.\" * }"
			+ "	    { \" and \" * editor #2 \"{vv~}{ll}\" format.name$ * }" + "	  if$" + "	}" + "      if$" + "    }"
			+ "  if$" + "}" + "" + "FUNCTION {format.book.crossref}" + "{ volume empty$"
			+ "    { \"empty volume in \" cite$ * \"'s crossref of \" * crossref * warning$" + "      \"In \"" + "    }"
			+ "    { \"Volume\" volume tie.or.space.connect" + "      \" of \" *" + "    }" + "  if$"
			+ "  editor empty$" + "  editor field.or.null author field.or.null =" + "  or" + "    { key empty$"
			+ "	{ series empty$" + "	    { \"need editor, key, or series for \" cite$ * \" to crossref \" *"
			+ "	      crossref * warning$" + "	      \"\" *" + "	    }"
			+ "	    { \"{\\em \" * series * \"\\/}\" * }" + "	  if$" + "	}" + "	{ key * }" + "      if$" + "    }"
			+ "    { format.crossref.editor * }" + "  if$" + "  \" \\cite{\" * crossref * \"}\" *" + "}" + ""
			+ "FUNCTION {format.incoll.inproc.crossref}" + "{ editor empty$"
			+ "  editor field.or.null author field.or.null =" + "  or" + "    { key empty$" + "	{ booktitle empty$"
			+ "	    { \"need editor, key, or booktitle for \" cite$ * \" to crossref \" *"
			+ "	      crossref * warning$" + "	      \"\"" + "	    }"
			+ "	    { \"In {\\em \" booktitle * \"\\/}\" * }" + "	  if$" + "	}" + "	{ \"In \" key * }" + "      if$"
			+ "    }" + "    { \"In \" format.crossref.editor * }" + "  if$" + "  \" \\cite{\" * crossref * \"}\" *"
			+ "}" + "" + "" + "% DFK added" + "% top of stack is the string we want to be a quoted paragraph"
			+ "FUNCTION {format.quotedParagraph}" + "{ duplicate$ empty$" + "    { skip$ }"
			+ "    { \"<P><QUOTE> \" swap$ * \"  </QUOTE></P>\" *}" + "  if$" + "}" + "" + ""
			+ "% DFK added, to support comment, private, keyword, etc" + "% next-to-top is field name (eg, \"Comment\")"
			+ "% top is field value (eg, value of comment)" + "% both are popped; resulting top is either empty, "
			+ "%    or string describing field" + "FUNCTION {format.dfkfield}" + "{ duplicate$ empty$"
			+ "	{ pop$ pop$ \"\" }" + "	{ swap$ " + "          \"<strong> \" swap$ * \":</strong> \" * swap$ * }"
			+ "  if$" + "}" + "" + "% DFK added" + "FUNCTION {dfk.stuff}" + "{ new.block"
			+ "  \"Abstract\" abstract format.dfkfield format.quotedParagraph write$ newline$"
			+ "  \"Keyword\" keyword format.dfkfield format.quotedParagraph write$ newline$"
			+ "  \"Comment\" comment format.dfkfield format.quotedParagraph write$ newline$" + "}" + ""
			+ "% DFK: added a call to dfk.stuff in all entry-type functions below" + "" + "FUNCTION {article}"
			+ "{ output.bibitem" + "  format.authors \"author\" output.check" + "  new.block"
			+ "  format.title \"title\" output.check" + "  new.block" + "  crossref missing$"
			+ "    { journal emphasize \"journal\" output.check" + "      format.vol.num.pages output"
			+ "      format.date \"year\" output.check" + "    }" + "    { format.article.crossref output.nonnull"
			+ "      format.pages output" + "    }" + "  if$" + "  new.block" + "  note output" + "  fin.entry"
			+ "  dfk.stuff" + "}" + "" + "FUNCTION {book}" + "{ output.bibitem" + "  author empty$"
			+ "    { format.editors \"author and editor\" output.check }" + "    { format.authors output.nonnull"
			+ "      crossref missing$" + "	{ \"author and editor\" editor either.or.check }" + "	'skip$"
			+ "      if$" + "    }" + "  if$" + "  new.block" + "  format.btitle \"title\" output.check"
			+ "  crossref missing$" + "    { format.bvolume output" + "      new.block"
			+ "      format.number.series output" + "      new.sentence" + "      publisher \"publisher\" output.check"
			+ "      address output" + "    }" + "    { new.block" + "      format.book.crossref output.nonnull"
			+ "    }" + "  if$" + "  format.edition output" + "  format.date \"year\" output.check" + "  new.block"
			+ "  note output" + "  fin.entry" + "  dfk.stuff" + "}" + "" + "FUNCTION {booklet}" + "{ output.bibitem"
			+ "  format.authors output" + "  new.block" + "  format.title \"title\" output.check"
			+ "  howpublished address new.block.checkb" + "  howpublished output" + "  address output"
			+ "  format.date output" + "  new.block" + "  note output" + "  fin.entry" + "  dfk.stuff" + "}" + ""
			+ "FUNCTION {inbook}" + "{ output.bibitem" + "  author empty$"
			+ "    { format.editors \"author and editor\" output.check }" + "    { format.authors output.nonnull"
			+ "      crossref missing$" + "	{ \"author and editor\" editor either.or.check }" + "	'skip$"
			+ "      if$" + "    }" + "  if$" + "  new.block" + "  format.btitle \"title\" output.check"
			+ "  crossref missing$" + "    { format.bvolume output"
			+ "      format.chapter.pages \"chapter and pages\" output.check" + "      new.block"
			+ "      format.number.series output" + "      new.sentence" + "      publisher \"publisher\" output.check"
			+ "      address output" + "    }" + "    { format.chapter.pages \"chapter and pages\" output.check"
			+ "      new.block" + "      format.book.crossref output.nonnull" + "    }" + "  if$"
			+ "  format.edition output" + "  format.date \"year\" output.check" + "  new.block" + "  note output"
			+ "  fin.entry" + "  dfk.stuff" + "}" + "" + "FUNCTION {incollection}" + "{ output.bibitem"
			+ "  format.authors \"author\" output.check" + "  new.block" + "  format.title \"title\" output.check"
			+ "  new.block" + "  crossref missing$" + "    { format.in.ed.booktitle \"booktitle\" output.check"
			+ "      format.bvolume output" + "      format.number.series output" + "      format.chapter.pages output"
			+ "      new.sentence" + "      publisher \"publisher\" output.check" + "      address output"
			+ "      format.edition output" + "      format.date \"year\" output.check" + "    }"
			+ "    { format.incoll.inproc.crossref output.nonnull" + "      format.chapter.pages output" + "    }"
			+ "  if$" + "  new.block" + "  note output" + "  fin.entry" + "  dfk.stuff" + "}" + ""
			+ "FUNCTION {inproceedings}" + "{ output.bibitem" + "  format.authors \"author\" output.check"
			+ "  new.block" + "  format.title \"title\" output.check" + "  new.block" + "  crossref missing$"
			+ "    { format.in.ed.booktitle \"booktitle\" output.check" + "      format.bvolume output"
			+ "      format.number.series output" + "      format.pages output" + "      address empty$"
			+ "	{ organization publisher new.sentence.checkb" + "	  organization output" + "	  publisher output"
			+ "	  format.date \"year\" output.check" + "	}" + "	{ address output.nonnull"
			+ "	  format.date \"year\" output.check" + "	  new.sentence" + "	  organization output"
			+ "	  publisher output" + "	}" + "      if$" + "    }"
			+ "    { format.incoll.inproc.crossref output.nonnull" + "      format.pages output" + "    }" + "  if$"
			+ "  new.block" + "  note output" + "  fin.entry" + "  dfk.stuff" + "}" + ""
			+ "FUNCTION {conference} { inproceedings }" + "" + "FUNCTION {manual}" + "{ output.bibitem"
			+ "  author empty$" + "    { organization empty$" + "	'skip$" + "	{ organization output.nonnull"
			+ "	  address output" + "	}" + "      if$" + "    }" + "    { format.authors output.nonnull }" + "  if$"
			+ "  new.block" + "  format.btitle \"title\" output.check" + "  author empty$" + "    { organization empty$"
			+ "	{ address new.block.checka" + "	  address output" + "	}" + "	'skip$" + "      if$" + "    }"
			+ "    { organization address new.block.checkb" + "      organization output" + "      address output"
			+ "    }" + "  if$" + "  format.edition output" + "  format.date output" + "  new.block" + "  note output"
			+ "  fin.entry" + "  dfk.stuff" + "}" + "" + "FUNCTION {mastersthesis}" + "{ output.bibitem"
			+ "  format.authors \"author\" output.check" + "  new.block" + "  format.title \"title\" output.check"
			+ "  new.block" + "  \"Master's thesis\" format.thesis.type output.nonnull"
			+ "  school \"school\" output.check" + "  address output" + "  format.date \"year\" output.check"
			+ "  new.block" + "  note output" + "  fin.entry" + "  dfk.stuff" + "}" + "" + "FUNCTION {misc}"
			+ "{ output.bibitem" + "  format.authors output" + "  title howpublished new.block.checkb"
			+ "  format.title output" + "  howpublished new.block.checka" + "  howpublished output"
			+ "  format.date output" + "  new.block" + "  note output" + "  fin.entry" + "  dfk.stuff"
			+ "  empty.misc.check" + "}" + "" + "FUNCTION {phdthesis}" + "{ output.bibitem"
			+ "  format.authors \"author\" output.check" + "  new.block" + "  format.btitle \"title\" output.check"
			+ "  new.block" + "  \"PhD thesis\" format.thesis.type output.nonnull" + "  school \"school\" output.check"
			+ "  address output" + "  format.date \"year\" output.check" + "  new.block" + "  note output"
			+ "  fin.entry" + "  dfk.stuff" + "}" + "" + "FUNCTION {proceedings}" + "{ output.bibitem"
			+ "  editor empty$" + "    { organization output }" + "    { format.editors output.nonnull }" + "  if$"
			+ "  new.block" + "  format.btitle \"title\" output.check" + "  format.bvolume output"
			+ "  format.number.series output" + "  address empty$" + "    { editor empty$"
			+ "	{ publisher new.sentence.checka }" + "	{ organization publisher new.sentence.checkb"
			+ "	  organization output" + "	}" + "      if$" + "      publisher output"
			+ "      format.date \"year\" output.check" + "    }" + "    { address output.nonnull"
			+ "      format.date \"year\" output.check" + "      new.sentence" + "      editor empty$" + "	'skip$"
			+ "	{ organization output }" + "      if$" + "      publisher output" + "    }" + "  if$" + "  new.block"
			+ "  note output" + "  fin.entry" + "  dfk.stuff" + "}" + "" + "FUNCTION {techreport}" + "{ output.bibitem"
			+ "  format.authors \"author\" output.check" + "  new.block" + "  format.title \"title\" output.check"
			+ "  new.block" + "  format.tr.number output.nonnull" + "  institution \"institution\" output.check"
			+ "  address output" + "  format.date \"year\" output.check" + "  new.block" + "  note output"
			+ "  fin.entry" + "  dfk.stuff" + "}" + "" + "FUNCTION {unpublished}" + "{ output.bibitem"
			+ "  format.authors \"author\" output.check" + "  new.block" + "  format.title \"title\" output.check"
			+ "  new.block" + "  note \"note\" output.check" + "  format.date output" + "  fin.entry" + "  dfk.stuff"
			+ "}" + "" + "FUNCTION {default.type} { misc }" + "" + "MACRO {jan} {\"January\"}" + ""
			+ "MACRO {feb} {\"February\"}" + "" + "MACRO {mar} {\"March\"}" + "" + "MACRO {apr} {\"April\"}" + ""
			+ "MACRO {may} {\"May\"}" + "" + "MACRO {jun} {\"June\"}" + "" + "MACRO {jul} {\"July\"}" + ""
			+ "MACRO {aug} {\"August\"}" + "" + "MACRO {sep} {\"September\"}" + "" + "MACRO {oct} {\"October\"}" + ""
			+ "MACRO {nov} {\"November\"}" + "" + "MACRO {dec} {\"December\"}" + ""
			+ "MACRO {acmcs} {\"ACM Computing Surveys\"}" + "" + "MACRO {acta} {\"Acta Informatica\"}" + ""
			+ "MACRO {cacm} {\"Communications of the ACM\"}" + ""
			+ "MACRO {ibmjrd} {\"IBM Journal of Research and Development\"}" + ""
			+ "MACRO {ibmsj} {\"IBM Systems Journal\"}" + ""
			+ "MACRO {ieeese} {\"IEEE Transactions on Software Engineering\"}" + ""
			+ "MACRO {ieeetc} {\"IEEE Transactions on Computers\"}" + "" + "MACRO {ieeetcad}"
			+ " {\"IEEE Transactions on Computer-Aided Design of Integrated Circuits\"}" + ""
			+ "MACRO {ipl} {\"Information Processing Letters\"}" + "" + "MACRO {jacm} {\"Journal of the ACM\"}" + ""
			+ "MACRO {jcss} {\"Journal of Computer and System Sciences\"}" + ""
			+ "MACRO {scp} {\"Science of Computer Programming\"}" + ""
			+ "MACRO {sicomp} {\"SIAM Journal on Computing\"}" + ""
			+ "MACRO {tocs} {\"ACM Transactions on Computer Systems\"}" + ""
			+ "MACRO {tods} {\"ACM Transactions on Database Systems\"}" + ""
			+ "MACRO {tog} {\"ACM Transactions on Graphics\"}" + ""
			+ "MACRO {toms} {\"ACM Transactions on Mathematical Software\"}" + ""
			+ "MACRO {toois} {\"ACM Transactions on Office Information Systems\"}" + ""
			+ "MACRO {toplas} {\"ACM Transactions on Programming Languages and Systems\"}" + ""
			+ "MACRO {tcs} {\"Theoretical Computer Science\"}" + "" + "READ" + "" + "FUNCTION {sortify}" + "{ purify$"
			+ "  \"l\" change.case$" + "}" + "" + "INTEGERS { len }" + "" + "FUNCTION {chop.word}" + "{ 's :="
			+ "  'len :=" + "  s #1 len substring$ =" + "    { s len #1 + global.max$ substring$ }" + "    's" + "  if$"
			+ "}" + "" + "INTEGERS { et.al.char.used }" + "" + "FUNCTION {initialize.et.al.char.used}"
			+ "{ #0 'et.al.char.used :=" + "}" + "" + "EXECUTE {initialize.et.al.char.used}" + ""
			+ "FUNCTION {format.lab.names}" + "{ 's :=" + "  s num.names$ 'numnames :=" + "  numnames #1 >"
			+ "    { numnames #4 >" + "	{ #3 'namesleft := }" + "	{ numnames 'namesleft := }" + "      if$"
			+ "      #1 'nameptr :=" + "      \"\"" + "	{ namesleft #0 > }" + "	{ nameptr numnames ="
			+ "	    { s nameptr \"{ff }{vv }{ll}{ jj}\" format.name$ \"others\" =" + "		{ \"{\\etalchar{+}}\" *"
			+ "		  #1 'et.al.char.used :=" + "		}" + "		{ s nameptr \"{v{}}{l{}}\" format.name$ * }"
			+ "	      if$" + "	    }" + "	    { s nameptr \"{v{}}{l{}}\" format.name$ * }" + "	  if$"
			+ "	  nameptr #1 + 'nameptr :=" + "	  namesleft #1 - 'namesleft :=" + "	}" + "      while$"
			+ "      numnames #4 >" + "	{ \"{\\etalchar{+}}\" *" + "	  #1 'et.al.char.used :=" + "	}" + "	'skip$"
			+ "      if$" + "    }" + "    { s #1 \"{v{}}{l{}}\" format.name$" + "      duplicate$ text.length$ #2 <"
			+ "	{ pop$ s #1 \"{ll}\" format.name$ #3 text.prefix$ }" + "	'skip$" + "      if$" + "    }" + "  if$"
			+ "}" + "" + "FUNCTION {author.key.label}" + "{ author empty$" + "    { key empty$"
			+ "	{ cite$ #1 #3 substring$ }" + "	{ key #3 text.prefix$ }" + "      if$" + "    }"
			+ "    { author format.lab.names }" + "  if$" + "}" + "" + "FUNCTION {author.editor.key.label}"
			+ "{ author empty$" + "    { editor empty$" + "	{ key empty$" + "	    { cite$ #1 #3 substring$ }"
			+ "	    { key #3 text.prefix$ }" + "	  if$" + "	}" + "	{ editor format.lab.names }" + "      if$"
			+ "    }" + "    { author format.lab.names }" + "  if$" + "}" + ""
			+ "FUNCTION {author.key.organization.label}" + "{ author empty$" + "    { key empty$"
			+ "	{ organization empty$" + "	    { cite$ #1 #3 substring$ }"
			+ "	    { \"The \" #4 organization chop.word #3 text.prefix$ }" + "	  if$" + "	}"
			+ "	{ key #3 text.prefix$ }" + "      if$" + "    }" + "    { author format.lab.names }" + "  if$" + "}"
			+ "" + "FUNCTION {editor.key.organization.label}" + "{ editor empty$" + "    { key empty$"
			+ "	{ organization empty$" + "	    { cite$ #1 #3 substring$ }"
			+ "	    { \"The \" #4 organization chop.word #3 text.prefix$ }" + "	  if$" + "	}"
			+ "	{ key #3 text.prefix$ }" + "      if$" + "    }" + "    { editor format.lab.names }" + "  if$" + "}"
			+ "" + "FUNCTION {calc.label}" + "{ type$ \"book\" =" + "  type$ \"inbook\" =" + "  or"
			+ "    'author.editor.key.label" + "    { type$ \"proceedings\" =" + "	'editor.key.organization.label"
			+ "	{ type$ \"manual\" =" + "	    'author.key.organization.label" + "	    'author.key.label" + "	  if$"
			+ "	}" + "      if$" + "    }" + "  if$" + "  duplicate$" + "  year field.or.null purify$ #-1 #2 substring$"
			+ "  *" + "  'label :=" + "  year field.or.null purify$ #-1 #4 substring$" + "  *"
			+ "  sortify 'sort.label :=" + "}" + "" + "FUNCTION {sort.format.names}" + "{ 's :=" + "  #1 'nameptr :="
			+ "  \"\"" + "  s num.names$ 'numnames :=" + "  numnames 'namesleft :=" + "    { namesleft #0 > }"
			+ "    { nameptr #1 >" + "	{ \"   \" * }" + "	'skip$" + "      if$"
			+ "      s nameptr \"{vv{ } }{ll{ }}{  ff{ }}{  jj{ }}\" format.name$ 't :="
			+ "      nameptr numnames = t \"others\" = and" + "	{ \"et al\" * }" + "	{ t sortify * }" + "      if$"
			+ "      nameptr #1 + 'nameptr :=" + "      namesleft #1 - 'namesleft :=" + "    }" + "  while$" + "}" + ""
			+ "FUNCTION {sort.format.title}" + "{ 't :=" + "  \"A \" #2" + "    \"An \" #3"
			+ "      \"The \" #4 t chop.word" + "    chop.word" + "  chop.word" + "  sortify"
			+ "  #1 global.max$ substring$" + "}" + "" + "FUNCTION {author.sort}" + "{ author empty$"
			+ "    { key empty$" + "	{ \"to sort, need author or key in \" cite$ * warning$" + "	  \"\"" + "	}"
			+ "	{ key sortify }" + "      if$" + "    }" + "    { author sort.format.names }" + "  if$" + "}" + ""
			+ "FUNCTION {author.editor.sort}" + "{ author empty$" + "    { editor empty$" + "	{ key empty$"
			+ "	    { \"to sort, need author, editor, or key in \" cite$ * warning$" + "	      \"\"" + "	    }"
			+ "	    { key sortify }" + "	  if$" + "	}" + "	{ editor sort.format.names }" + "      if$" + "    }"
			+ "    { author sort.format.names }" + "  if$" + "}" + "FUNCTION {author.organization.sort}"
			+ "{ author empty$" + "    { organization empty$" + "	{ key empty$"
			+ "	    { \"to sort, need author, organization, or key in \" cite$ * warning$" + "	      \"\"" + "	    }"
			+ "	    { key sortify }" + "	  if$" + "	}" + "	{ \"The \" #4 organization chop.word sortify }"
			+ "      if$" + "    }" + "    { author sort.format.names }" + "  if$" + "}"
			+ "FUNCTION {editor.organization.sort}" + "{ editor empty$" + "    { organization empty$"
			+ "	{ key empty$" + "	    { \"to sort, need editor, organization, or key in \" cite$ * warning$"
			+ "	      \"\"" + "	    }" + "	    { key sortify }" + "	  if$" + "	}"
			+ "	{ \"The \" #4 organization chop.word sortify }" + "      if$" + "    }"
			+ "    { editor sort.format.names }" + "  if$" + "}" + "FUNCTION {presort}" + "{ calc.label"
			+ "  sort.label" + "  \"    \"" + "  *" + "  type$ \"book\" =" + "  type$ \"inbook\" =" + "  or"
			+ "    'author.editor.sort" + "    { type$ \"proceedings\" =" + "	'editor.organization.sort"
			+ "	{ type$ \"manual\" =" + "	    'author.organization.sort" + "	    'author.sort" + "	  if$" + "	}"
			+ "      if$" + "    }" + "  if$" + "  *" + "  \"    \"" + "  *" + "  year field.or.null sortify" + "  *"
			+ "  \"    \"" + "  *" + "  title field.or.null" + "  sort.format.title" + "  *" + "  pop$" + "  cite$"
			+ "  'sort.key$ :=" + "}" + "ITERATE {presort}" + "SORT"
			+ "STRINGS { longest.label last.sort.label next.extra }" + "INTEGERS { longest.label.width last.extra.num }"
			+ "FUNCTION {initialize.longest.label}" + "{ \"\" 'longest.label :=" + "  \"\" 'next.extra :=" + "}"
			+ "FUNCTION {forward.pass}" + "{ last.sort.label sort.label ="
			+ "    { last.extra.num #1 + 'last.extra.num :=" + "      last.extra.num int.to.chr$ 'extra.label :="
			+ "    }" + "    { \"a\" chr.to.int$ 'last.extra.num :=" + "      \"\" 'extra.label :="
			+ "      sort.label 'last.sort.label :=" + "    }" + "  if$" + "}" + "FUNCTION {reverse.pass}"
			+ "{ next.extra \"b\" =" + "    { \"a\" 'extra.label := }" + "    'skip$" + "  if$"
			+ "  label extra.label * 'label :=" + "  label width$ longest.label.width >"
			+ "    { label 'longest.label :=" + "      label width$ 'longest.label.width :=" + "    }" + "    'skip$"
			+ "  if$" + "  extra.label 'next.extra :=" + "}" + "EXECUTE {initialize.longest.label}"
			+ "ITERATE {forward.pass}" + "REVERSE {reverse.pass}" + "FUNCTION {begin.bib}"
			+ "{ \"<HTML>\" write$ newline$" + "  \"<HEAD><TITLE> Bibliography </TITLE></HEAD>\" write$ newline$"
			+ "  \"<BODY BGCOLOR=#EEEEEE>\" write$ newline$" + "  \"<DL>\" write$ newline$" + "}"
			+ "EXECUTE {begin.bib}" + "EXECUTE {init.state.consts}" + "ITERATE {call.type$}" + "FUNCTION {end.bib}"
			+ "{ newline$" + "  \"</DL>\" write$ newline$" + "  \"</BODY>\" write$ newline$"
			+ "  \"</HTML>\" write$ newline$" + "}" + "EXECUTE {end.bib}";

	String perlData = "use File::Basename;\n"
			+ "$usage = 'usage: $0 {alpha\\index\\long\\longp\\long-pario\\short\\short-pario\\split} [-o <outfile>] file.bib...';\n"
			+ "$tmp = \"/tmp/bib2html$$\";\n" + "if ( scalar(@ARGV) < 2 ) {\n" + "    die \"$usage\\n\";\n" + "}\n"
			+ "my %Opts;\n" + "my $files;\n" + "$Opts{style} = shift @ARGV;\n"
			+ "if ( ($tmpArg = shift @ARGV) eq \"-o\" ) {\n" + "    $Opts{outfile} = shift @ARGV;\n"
			+ "    $files = killSuffix ( shift @ARGV );\n" + "} else { $files = killSuffix ( $tmpArg ); }\n"
			+ "$Opts{outfile} = \"bib.html\" if ( !defined ($Opts{outfile}) );\n" + "foreach $file ( @ARGV ) {\n"
			+ "    $files .= ','; $files .= killSuffix ($file);\n" + "}\n" + "$SIG{INT} = \\&cleanup;\n"
			+ "print \"Creating $tmp.aux for $files\\n\";\n" + "open AUX, \">$tmp.aux\";\n" + "print AUX <<EOF;\n"
			+ "\\\\relax \n" + "\\\\citation{*}\n" + "\\\\bibstyle{html-$Opts{style}}\n" + "\\\\bibdata{$files}\n"
			+ "EOF\n" + "close AUX;\n" + "unlink <$tmp.{bbl,blg}>;\n" + "if (-r \"html-split.bst.gz\" ) {\n"
			+ "    system ( \"gunzip html-split.bst.gz\" );\n" + "}\n" + "print \"bibtex $tmp\\n\";\n"
			+ "system \"bibtex $tmp\";\n" + "print \"Bibtex done\\n\\n\";\n" + "open BBL, \"$tmp.bbl\";\n"
			+ "open OUT, \">$Opts{outfile}\";\n" + "my @formatsToClose;\n" + "while ( <BBL> ) { \n"
			+ "    s/\\\\ \\` (?: \\{ )? ([aeiouAEIOU]) (?: \\} )?\n" + "          /&$1grave;/gx;\n"
			+ "    s/ \\\\ \\' (?: \\{ )? ([aeiouAEIOU]) (?: \\} )?\n" + "          /&$1acute;/gx;\n"
			+ "    s/ \\\\ \\^ (?: \\{ )? ([aeiouAEIOU]) (?: \\} )?\n" + "          /&$1circ;/gx;\n"
			+ "    s/ \\\\ \\\" (?: \\{ )? ([aeiouyAEIOUY]) (?: \\} )?\n" + "          /&$1uml;/gx;\n"
			+ "    s/ \\\\ \\~ (?: \\{ )? ([anoANO]) (?: \\} )?\n" + "          /&$1tilde;/gx;\n"
			+ "    s/ \\\\ c (?: \\{ )? ([cC]) (?: \\} )?\n" + "          /&$1cedil;/gx;\n" + "    s/ \\\\copyright\n"
			+ "	/&copy;/gx;\n" + "    s/ \\\\pounds\n" + "	/&pound;/gx;\n" + "    s/ \\\\ (ae\\AE)\n"
			+ "	/&$1lig;/gx;\n"
			+ "    s/ \\\\ (?: var )? (alpha|beta|gamma|delta|epsilon|theta|lambda|pi|rho|sigma|omega)\n"
			+ "	/&$1;/gxi;\n" + "    s+ ([^\\\\]) \\\\ \\/\n" + "	+$1+gx;\n"
			+ "    s+ \\{ \\\\ (?: (em)|(tt)|(b)f|(i)t )\\ (.*?) \\}\n"
			+ "        + join '', (\"<\", ($1 or $2 or $3 or $4), \">\", $5, \"</\", ($1 or $2 or $3 or $4), \">\") +gex;\n"
			+ "    s+ ([^\\\\]) \\\\cite\\{ (.*?) \\}\n" + "        +$1<a href=\"#$2\">$2</a>+xg;\n"
			+ "    s/\\\\ie/i.e./g;\n" + "    s/\\\\eg/e.g./g;\n" + "    s/\\\\etc/etc./g;\n"
			+ "    s+\\\\vs\\\\+<EM>vs.</EM>+g;\n" + "    s/\\\\usec/usec/g;\n" + "    s/\\\\mbox //g;\n"
			+ "    s/\\\\par / <P> /g;\n" + "    s/\\\\par$/ <P>/g;\n" + "    s/\\\\\\&/\\&amp;/g;\n"
			+ "    s/-{2,3}/-/g;\n" + "    if ( s/ ([^\\\\]) \\% \\n$ /$1/x and $nextline = <BBL> ) {\n"
			+ "	$_ .= $nextline;\n" + "	redo;\n" + "    }\n" + "    if ( / \\{ \\\\ (em\\tt\\bf\\it) .*\\n$ /x ) {\n"
			+ "	while ( s/ \\{ \\\\ (?: (em)|(b)f|(tt)|(i)t ) (?: \\ (.*\\n) )? $\n"
			+ "	       / join '', (\"<\", ($1 or $2 or $3 or $4), \">\", ($5 or \"\") ) /ex )\n" + "	{\n"
			+ "	    push ( @formatsToClose, ($1 or $2 or $3 or $4) );\n" + "	}\n"
			+ "	if ( $nextline = <BBL> ) {\n" + "	    $_ .= $nextline;\n" + "	    redo;\n" + "	}\n" + "    }    \n"
			+ "    if ( scalar (@formatsToClose) > 0 ) {\n" + "	while ( s+ ([^\\\\]) \\}\n"
			+ "	       + join '', ($1, \"</\", ($format = pop (@formatsToClose)), \">\" ) +ex )\n" + "	{\n" + "	}\n"
			+ "    }\n" + "    s+ ([^\\\\/]) ~ +$1&nbsp;+xg;	# normal standalone tilde - nbsp\n"
			+ "    s/ \\\\~ \\{\\} /~/xg;		# \\~{} to ~\n" + "    my $escapedChars = quotemeta ( '#$%&_{}' );\n"
			+ "    s/ ([^\\\\]) \\\\ ([$escapedChars]) \n" + "        /$1$2/gxo;\n" + "    print OUT $_;\n" + "}\n"
			+ "print \"\\n\";\n" + "print \"\\noutput is in $Opts{outfile}\\n\";\n" + "cleanup();\n"
			+ "sub killSuffix {\n" + "    $file = shift();\n"
			+ "    ( $name, $path ) = fileparse ( $file, '\\.[^.]*$' );\n" + "    return ($path . $name);\n" + "}\n"
			+ "sub cleanup { unlink ( glob (\"$tmp.{aux,bbl,blg}\") ); }\n";
	{
		boolean errord = false;
		try {
			File f1 = File.createTempFile("bib2html", "pl");
			File f2 = File.createTempFile("html-long", "bst");
			try {
				f1.delete();
				f2.delete();
			} catch (Exception e) {
				errord = true;
			}
			PrintWriter perlw = new PrintWriter(new FileWriter(f1));
			PrintWriter bibw = new PrintWriter(new FileWriter(f2));
			perlw.print(perlData);
			bibw.print(bibData);
			perlw.close();
			bibw.close();
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
		File f1 = File.createTempFile("bib2html", "pl");
		File f2 = File.createTempFile("html-long", "bst");
		boolean errord = false;
		try {
			f1.delete();
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

	public BIB2HTML() throws Exception {
		try {
			NativeExec.execute("perl -h");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: perl");
		}
		try {
			NativeExec.execute("bibtex --help ");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: bibtex");
		}

	}

	/**
	 * Description of the Method
	 *
	 * @param s2
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertBIBToHTML(String s2) throws Exception {
		String filename = "" + System.currentTimeMillis() + "." + rnd.nextInt();
		try {
			String aux = new String();
			sizeCount = s2.length();
			java.io.File file1 = java.io.File.createTempFile(filename, "bib");
			java.io.File file2 = java.io.File.createTempFile(filename, "html");
			PrintWriter out = new PrintWriter(new FileWriter(file1));
			out.print(s2);
			out.close();
			NativeExec.execute("perl " + scriptfile.getAbsolutePath() + " long -o " + file2.getAbsolutePath() + " "
					+ file1.getAbsolutePath());
			BufferedReader input = new BufferedReader(new FileReader(file2));
			while ((s2 = input.readLine()) != null) {
				aux += s2 + "\n";
			}
			input.close();
			file1.delete();
			file2.delete();
			if (aux.equals("")) {
				throw new Exception("Empty Content.");
			}
			return aux;
		} catch (Exception e) {
			boolean errord = false;
			try {
				(java.io.File.createTempFile(filename, "bib")).delete();
			} catch (Exception e2) {
				errord = true;
			}
			try {
				(java.io.File.createTempFile(filename, "html")).delete();
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
	public String convertBIBToHTML(Reader input) throws Exception {
		BufferedReader strm = new BufferedReader(input);
		StringBuffer sb = new StringBuffer();
		String s;
		while ((s = strm.readLine()) != null) {
			sb.append(s);
			sb.append('\n');
		}
		return convertBIBToHTML(sb.toString());
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
	public String convertBIBToHTML(InputStream input) throws Exception {
		BufferedReader strm = new BufferedReader(new InputStreamReader(input));
		StringBuffer sb = new StringBuffer();
		String s;
		while ((s = strm.readLine()) != null) {
			sb.append(s);
			sb.append('\n');
		}
		return convertBIBToHTML(sb.toString());
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
	public String convertBIBToHTML(java.io.File input) throws Exception {
		BufferedReader strm = new BufferedReader(new FileReader(input));
		StringBuffer sb = new StringBuffer();
		String s;
		while ((s = strm.readLine()) != null) {
			sb.append(s);
			sb.append('\n');
		}
		return convertBIBToHTML(sb.toString());
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
	public String convertBIBToHTML(URL input) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) input.openConnection();
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows 98");
		conn.setInstanceFollowRedirects(true);
		conn.connect();
		BufferedReader strm = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String s;
		while ((s = strm.readLine()) != null) {
			sb.append(s);
			sb.append('\n');
		}
		return convertBIBToHTML(sb.toString());
	}

}
