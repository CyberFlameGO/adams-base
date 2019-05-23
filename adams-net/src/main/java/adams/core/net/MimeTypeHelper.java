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
 * MimeTypeHelper.java
 * Copyright (C) 2013-2019 University of Waikato, Hamilton, New Zealand
 */
package adams.core.net;

import adams.core.io.FileUtils;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Helper class for mime types.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class MimeTypeHelper {

  /** mime type: application/octet-stream. */
  public final static String MIMETYPE_APPLICATION_OCTETSTREAM = "application/octet-stream";

  /** mime type: plain/text. */
  public final static String MIMETYPE_PLAIN_TEXT = "plain/text";

  /**
   * Tries to determine the MIME type of a file by checking its magic bytes.
   * Taken from here:
   * <a href="http://stackoverflow.com/a/16626396" target="_blank">http://stackoverflow.com/a/16626396</a>.
   * 
   * @param file	the file to check
   * @return		the MIME type, {@link MediaType#OCTET_STREAM} if it fails
   */
  public static MediaType getMimeType(File file) {
    return MimeTypeHelper.getMimeType(file.getAbsolutePath());
  }

  /**
   * Tries to determine the MIME type of a file by checking its magic bytes.
   * Taken from here:
   * <a href="http://stackoverflow.com/a/16626396" target="_blank">http://stackoverflow.com/a/16626396</a>.
   * 
   * @param filename	the file to check
   * @return		the MIME type, {@link MediaType#OCTET_STREAM} if it fails
   */
  public static MediaType getMimeType(String filename) {
    MediaType 			result;
    FileInputStream             fis;
    BufferedInputStream 	bis;
    AutoDetectParser 		parser;
    Detector 			detector;
    Metadata 			md;

    fis = null;
    bis = null;
    try {
      fis      = new FileInputStream(filename);
      bis      = new BufferedInputStream(fis);
      parser   = new AutoDetectParser();
      detector = parser.getDetector();
      md       = new Metadata();
      md.add(Metadata.RESOURCE_NAME_KEY, filename);
      result = detector.detect(bis, md);
      bis.close();
      return result;
    }
    catch (Exception e) {
      return MediaType.OCTET_STREAM;
    }
    finally {
      FileUtils.closeQuietly(bis);
      FileUtils.closeQuietly(fis);
    }
  }
}
