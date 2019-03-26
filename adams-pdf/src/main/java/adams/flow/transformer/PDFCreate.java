/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * PDFCreate.java
 * Copyright (C) 2009-2019 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.transformer;

import adams.core.QuickInfoHelper;
import adams.core.io.FileUtils;
import adams.core.io.FileWriter;
import adams.core.io.PlaceholderFile;
import adams.data.PageOrientation;
import adams.flow.core.Token;
import adams.flow.transformer.pdfproclet.Image;
import adams.flow.transformer.pdfproclet.PDFGenerator;
import adams.flow.transformer.pdfproclet.PageSize;
import adams.flow.transformer.pdfproclet.PdfProclet;
import adams.flow.transformer.pdfproclet.PlainText;
import adams.flow.transformer.pdfproclet.SpreadSheet;

import java.io.File;

/**
 <!-- globalinfo-start -->
 * Actor for generating PDF files. Images (GIF&#47;PNG&#47;JPEG), plain text files and CSV files are supported. CSV files are automatically added as tables (if they contain comments, then these will get added as well).<br>
 * The filename of the generated PDF is forwarded.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;java.lang.String<br>
 * &nbsp;&nbsp;&nbsp;java.lang.String[]<br>
 * &nbsp;&nbsp;&nbsp;java.io.File<br>
 * &nbsp;&nbsp;&nbsp;java.io.File[]<br>
 * - generates:<br>
 * &nbsp;&nbsp;&nbsp;java.lang.String<br>
 * <br><br>
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: PDFCreate
 * </pre>
 * 
 * <pre>-annotation &lt;adams.core.base.BaseAnnotation&gt; (property: annotations)
 * &nbsp;&nbsp;&nbsp;The annotations to attach to this actor.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-skip &lt;boolean&gt; (property: skip)
 * &nbsp;&nbsp;&nbsp;If set to true, transformation is skipped and the input token is just forwarded 
 * &nbsp;&nbsp;&nbsp;as it is.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-stop-flow-on-error &lt;boolean&gt; (property: stopFlowOnError)
 * &nbsp;&nbsp;&nbsp;If set to true, the flow gets stopped in case this actor encounters an error;
 * &nbsp;&nbsp;&nbsp; useful for critical actors.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-silent &lt;boolean&gt; (property: silent)
 * &nbsp;&nbsp;&nbsp;If enabled, then no errors are output in the console; Note: the enclosing 
 * &nbsp;&nbsp;&nbsp;actor handler must have this enabled as well.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-output &lt;adams.core.io.PlaceholderFile&gt; (property: output)
 * &nbsp;&nbsp;&nbsp;The PDF file to generate.
 * &nbsp;&nbsp;&nbsp;default: ${CWD}
 * </pre>
 * 
 * <pre>-page-size &lt;A0|A1|A10|A2|A3|A4|A5|A6|A7|A8|A9|ARCH_A|ARCH_B|ARCH_C|ARCH_D|ARCH_E|B0|B1|B10|B2|B3|B4|B5|B6|B7|B8|B9|CROWN_OCTAVO|CROWN_QUARTO|DEMY_OCTAVO|DEMY_QUARTO|EXECUTIVE|FLSA|FLSE|HALFLETTER|ID_1|ID_2|ID_3|LARGE_CROWN_OCTAVO|LARGE_CROWN_QUARTO|LEDGER|LEGAL|LETTER|NOTE|PENGUIN_LARGE_PAPERBACK|PENGUIN_SMALL_PAPERBACK|POSTCARD|ROYAL_OCTAVO|ROYAL_QUARTO|SMALL_PAPERBACK|TABLOID&gt; (property: pageSize)
 * &nbsp;&nbsp;&nbsp;The page size of the generated PDF.
 * &nbsp;&nbsp;&nbsp;default: A4
 * </pre>
 * 
 * <pre>-page-orientation &lt;PORTRAIT|LANDSCAPE&gt; (property: pageOrientation)
 * &nbsp;&nbsp;&nbsp;The page orientation of the generated PDF.
 * &nbsp;&nbsp;&nbsp;default: PORTRAIT
 * </pre>
 * 
 * <pre>-proclet &lt;adams.flow.transformer.pdfproclet.PdfProclet&gt; [-proclet ...] (property: proclets)
 * &nbsp;&nbsp;&nbsp;The processors for processing the files.
 * &nbsp;&nbsp;&nbsp;default: adams.flow.transformer.pdfproclet.PlainText, adams.flow.transformer.pdfproclet.SpreadSheet -reader \"adams.data.io.input.CsvSpreadSheetReader -data-row-type adams.data.spreadsheet.DenseDataRow -spreadsheet-type adams.data.spreadsheet.DefaultSpreadSheet\", adams.flow.transformer.pdfproclet.Image
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class PDFCreate
  extends AbstractTransformer
  implements FileWriter {

  /** for serialization. */
  private static final long serialVersionUID = 5783362940767103716L;

  /** the output file. */
  protected PlaceholderFile m_OutputFile;

  /** the page size. */
  protected PageSize m_PageSize;

  /** the page orientation. */
  protected PageOrientation m_PageOrientation;

  /** the PDF processors. */
  protected PdfProclet[] m_Proclets;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
        "Actor for generating PDF files. Images (GIF/PNG/JPEG), plain text "
      + "files and CSV files are supported. CSV files are automatically added "
      + "as tables (if they contain comments, then these will get added as well).\n"
      + "The filename of the generated PDF is forwarded.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	    "output", "outputFile",
	    new PlaceholderFile("."));

    m_OptionManager.add(
	    "page-size", "pageSize",
	    PageSize.A4);

    m_OptionManager.add(
	    "page-orientation", "pageOrientation",
	    PageOrientation.PORTRAIT);

    m_OptionManager.add(
	    "proclet", "proclets",
	    new PdfProclet[]{
		new PlainText(),
		new SpreadSheet(),
		new Image()});
  }

  /**
   * Sets the output file.
   *
   * @param value	the file
   */
  public void setOutputFile(PlaceholderFile value) {
    m_OutputFile = value;
    reset();
  }

  /**
   * Returns the output file.
   *
   * @return 		the file
   */
  public PlaceholderFile getOutputFile() {
    return m_OutputFile;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return         tip text for this property suitable for
   *             displaying in the GUI or for listing the options.
   */
  public String outputFileTipText() {
    return "The PDF file to generate.";
  }

  /**
   * Sets the page size.
   *
   * @param value	the size
   */
  public void setPageSize(PageSize value) {
    m_PageSize = value;
    reset();
  }

  /**
   * Returns the page size.
   *
   * @return 		the size
   */
  public PageSize getPageSize() {
    return m_PageSize;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return         tip text for this property suitable for
   *             displaying in the GUI or for listing the options.
   */
  public String pageSizeTipText() {
    return "The page size of the generated PDF.";
  }

  /**
   * Sets the page orientation.
   *
   * @param value	the orientation
   */
  public void setPageOrientation(PageOrientation value) {
    m_PageOrientation = value;
    reset();
  }

  /**
   * Returns the page orientation.
   *
   * @return 		the orientation
   */
  public PageOrientation getPageOrientation() {
    return m_PageOrientation;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return         tip text for this property suitable for
   *             displaying in the GUI or for listing the options.
   */
  public String pageOrientationTipText() {
    return "The page orientation of the generated PDF.";
  }

  /**
   * Sets the processors for processing the files.
   *
   * @param value	the processors to use
   */
  public void setProclets(PdfProclet[] value) {
    m_Proclets = value;
    reset();
  }

  /**
   * Returns the processors in use.
   *
   * @return 		the processors in use
   */
  public PdfProclet[] getProclets() {
    return m_Proclets;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return         tip text for this property suitable for
   *             displaying in the GUI or for listing the options.
   */
  public String procletsTipText() {
    return "The processors for processing the files.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result  = QuickInfoHelper.toString(this, "outputFile", m_OutputFile, "output: ");
    result += QuickInfoHelper.toString(this, "pageSize", m_PageSize, ", size: ");
    result += QuickInfoHelper.toString(this, "pageOrientation", m_PageOrientation, ", orientation: ");

    return result;
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		<!-- flow-accepts-start -->java.lang.String.class, java.lang.String[].class, java.io.File.class, java.io.File[].class<!-- flow-accepts-end -->
   */
  public Class[] accepts() {
    return new Class[]{String.class, String[].class, File.class, File[].class};
  }

  /**
   * Returns the class of objects that it generates.
   *
   * @return		<!-- flow-generates-start -->java.lang.String.class<!-- flow-generates-end -->
   */
  public Class[] generates() {
    return new Class[]{String.class};
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute() {
    String		result;
    PlaceholderFile[]	files;
    PDFGenerator generator;

    result = null;

    // get files
    files = FileUtils.toPlaceholderFileArray(m_InputToken.getPayload());

    // create PDF document
    generator = new PDFGenerator();
    generator.setLoggingLevel(getLoggingLevel());
    generator.setOutput(getOutputFile());
    generator.setPageSize(getPageSize());
    generator.setPageOrientation(getPageOrientation());
    generator.setProclets(getProclets());
    try {
      generator.open();
      for (File file: files) {
	try {
	  generator.addFile(file);
	}
	catch (Exception e) {
	  handleException("Problems adding file '" + file + "'!", e);
	}
      }
    }
    catch (Exception e) {
      result = handleException("Failed to create PDF: " + m_OutputFile, e);
    }
    finally {
      generator.close();
    }

    m_OutputToken = new Token(m_OutputFile.getAbsolutePath());

    return result;
  }
}
