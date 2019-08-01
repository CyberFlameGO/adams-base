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
 * UnXz.java
 * Copyright (C) 2018-2019 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.transformer;

import adams.core.MessageCollection;
import adams.core.io.XzUtils;
import gnu.trove.list.TByteList;

import java.io.File;

/**
 <!-- globalinfo-start -->
 * Decompresses an archive that was compressed with XZ. It is assumed, that the file ends with .7z. If that is not the case, an alternative filename has to be provided.<br>
 * The filename of the generated output filename is then broadcasted.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;java.lang.String<br>
 * &nbsp;&nbsp;&nbsp;java.io.File<br>
 * - generates:<br>
 * &nbsp;&nbsp;&nbsp;java.lang.String<br>
 * <br><br>
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * Valid options are: <br><br>
 * 
 * <pre>-D &lt;int&gt; (property: debugLevel)
 * &nbsp;&nbsp;&nbsp;The greater the number the more additional info the scheme may output to 
 * &nbsp;&nbsp;&nbsp;the console (0 = off).
 * &nbsp;&nbsp;&nbsp;default: 0
 * &nbsp;&nbsp;&nbsp;minimum: 0
 * </pre>
 * 
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: UnXz
 * </pre>
 * 
 * <pre>-annotation &lt;adams.core.base.BaseText&gt; (property: annotations)
 * &nbsp;&nbsp;&nbsp;The annotations to attach to this actor.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-skip (property: skip)
 * &nbsp;&nbsp;&nbsp;If set to true, transformation is skipped and the input token is just forwarded 
 * &nbsp;&nbsp;&nbsp;as it is.
 * </pre>
 * 
 * <pre>-stop-flow-on-error (property: stopFlowOnError)
 * &nbsp;&nbsp;&nbsp;If set to true, the flow gets stopped in case this actor encounters an error;
 * &nbsp;&nbsp;&nbsp; useful for critical actors.
 * </pre>
 * 
 * <pre>-use-out-dir (property: useAlternativeOutputDir)
 * &nbsp;&nbsp;&nbsp;By default the compressed file will be uncompressed at the same location 
 * &nbsp;&nbsp;&nbsp;as the archive with the .7z extension; use this option to enabled the selection 
 * &nbsp;&nbsp;&nbsp;of a different output directory.
 * </pre>
 * 
 * <pre>-out-dir &lt;adams.core.io.PlaceholderDirectory&gt; (property: alternativeOutputDir)
 * &nbsp;&nbsp;&nbsp;The alternative output directory to use.
 * &nbsp;&nbsp;&nbsp;default: ${CWD}
 * </pre>
 * 
 * <pre>-alt-filename &lt;java.lang.String&gt; (property: alternativeFilename)
 * &nbsp;&nbsp;&nbsp;The alternative filename to use, instead of the one from the input file 
 * &nbsp;&nbsp;&nbsp;with the .7z extension removed.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-buffer &lt;int&gt; (property: bufferSize)
 * &nbsp;&nbsp;&nbsp;The size of the buffer in bytes for the data stream.
 * &nbsp;&nbsp;&nbsp;default: 1024
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class UnXz
  extends AbstractSingleDecompress {

  /** for serialization. */
  private static final long serialVersionUID = -8357597085805205924L;

  /**
   * Returns a string describing the object.
   *
   * @return 		a description suitable for displaying in the gui
   */
  public String globalInfo() {
    return
        "Decompresses an archive that was compressed with XZ. It is assumed, "
      + "that the file ends with " + XzUtils.EXTENSION + ". If that is not the "
      + "case, an alternative filename has to be provided.\n"
      + "The filename of the generated output filename is then broadcasted.";
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String useAlternativeOutputDirTipText() {
    return
        "By default the compressed file will be uncompressed at the same "
      + "location as the archive with the " + XzUtils.EXTENSION + " extension; "
      + "use this option to enabled the selection of a different output "
      + "directory.";
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *			displaying in the GUI or for listing the options.
   */
  public String alternativeFilenameTipText() {
    return
        "The alternative filename to use, instead of the one from the input "
      + "file with the " + XzUtils.EXTENSION + " extension removed.";
  }

  /**
   * Returns the default extension that the compressed archive has, e.g.,
   * ".7z" for compressed files.
   *
   * @return		the extension, including the dot
   */
  protected String getDefaultExtension() {
    return XzUtils.EXTENSION;
  }

  /**
   * Decompresses the archive.
   *
   * @param inFile	the compressed archive
   * @param outFile	the decompressed output file
   * @return		null if successful, otherwise error message
   */
  protected String decompress(File inFile, File outFile) {
    return XzUtils.decompress(inFile, m_BufferSize, outFile);
  }

  /**
   * Decompresses the bytes.
   *
   * @param inBytes	the compressed bytes
   * @param outBytes	the decompressed bytes
   * @return		null if successful, otherwise error message
   */
  protected String decompress(byte[] inBytes, TByteList outBytes) {
    byte[]	decompressed;
    MessageCollection errors;

    errors       = new MessageCollection();
    decompressed = XzUtils.decompress(inBytes, m_BufferSize, errors);
    if (decompressed == null) {
      return (errors.isEmpty() ? "Failed to compress!" : errors.toString());
    }
    else {
      outBytes.addAll(decompressed);
      return null;
    }
  }
}
