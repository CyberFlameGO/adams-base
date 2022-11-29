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
 * MatlabUtils.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package adams.data.matlab;

import us.hebi.matlab.mat.types.Array;
import us.hebi.matlab.mat.types.Char;
import us.hebi.matlab.mat.types.Matrix;

/**
 * Helper class for Matlab data structures.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class MatlabUtils {

  /**
   * Converts a character cell into a string. Rows are interpreted as lines.
   *
   * @param matChar	the cell to convert
   * @return		the generated string
   */
  public static String charToString(Char matChar) {
    StringBuilder 	result;
    int			cols;
    int			rows;
    int			x;
    int			y;

    result = new StringBuilder();

    rows = matChar.getNumRows();
    cols = matChar.getNumCols();

    for (y = 0; y < rows; y++) {
      if (y > 0)
	result.append("\n");
      for (x = 0; x < cols; x++)
	result.append(matChar.getChar(y, x));
    }

    return result.toString();
  }

  /**
   * Increments the index.
   *
   * @param index	the current index
   * @param dims 	the dimensions (ie max values)
   * @return		true if finished
   */
  public static boolean increment(int[] index, int[] dims) {
    int		pos;

    pos = index.length - 1;
    index[pos]++;
    while (index[pos] >= dims[pos]) {
      if (pos == 0)
	return true;
      index[pos] = 0;
      pos--;
      index[pos]++;
    }

    return false;
  }

  /**
   * Increments the index.
   *
   * @param index	the current index
   * @param dims 	the dimensions (ie max values)
   * @param open 	the indices that are "open" to be incremented
   * @return		true if finished
   */
  public static boolean increment(int[] index, int[] dims, int[] open) {
    int		pos;

    pos = open.length - 1;
    index[open[pos]]++;
    while (index[open[pos]] >= dims[open[pos]]) {
      if (pos == 0)
	return true;
      index[open[pos]] = 0;
      pos--;
      index[open[pos]]++;
    }

    return false;
  }

  /**
   * Transfers a single cell from one matrix to another.
   *
   * @param source	the source matrix
   * @param sourceIndex	the index of the cell in the source matrix
   * @param target	the target matrix
   * @param targetIndex	the index of the cell in the target matrix
   * @param type	the type of element to transfer
   */
  public static void transfer(Matrix source, int[] sourceIndex, Matrix target, int[] targetIndex, ArrayElementType type) {
    switch (type) {
      case BOOLEAN:
	target.setBoolean(targetIndex, source.getBoolean(sourceIndex));
	break;
      case BYTE:
	target.setByte(targetIndex, source.getByte(sourceIndex));
	break;
      case SHORT:
	target.setShort(targetIndex, source.getShort(sourceIndex));
	break;
      case INTEGER:
	target.setInt(targetIndex, source.getInt(sourceIndex));
	break;
      case LONG:
	target.setLong(targetIndex, source.getLong(sourceIndex));
	break;
      case FLOAT:
	target.setFloat(targetIndex, source.getFloat(sourceIndex));
	break;
      case DOUBLE:
	target.setDouble(targetIndex, source.getDouble(sourceIndex));
	break;
      default:
        throw new IllegalStateException("Unhandled element type: " + type);
    }
  }

  /**
   * For transferring the subset from the original matrix into the new one.
   *
   * @param source	the source matrix
   * @param dimsSource	the dimensions of the source matrix
   * @param openSource	the indices of the "open" dimensions
   * @param indexSource the indices in the source to use
   * @param target	the target matrix
   * @param dimsTarget	the dimensions of the target matrix
   * @param type	the element type to use for transferring the data
   */
  public static void transfer(Matrix source, int[] dimsSource, int[] openSource, int[] indexSource, Matrix target, int[] dimsTarget, ArrayElementType type) {
    int		i;
    int[] 	indexTarget;
    boolean	finished;

    finished    = false;
    indexTarget = new int[dimsTarget.length];

    while (!finished) {
      for (i = 0; i < indexTarget.length; i++)
	indexSource[openSource[i]] = indexTarget[i];

      transfer(source, indexSource, target, indexTarget, type);

      finished = increment(indexTarget, dimsTarget);
    }
  }

  /**
   * For transferring the subset from the original matrix into the new one.
   *
   * @param source	the source matrix
   * @param indexSource the source index template to use, initializes with all 0s if null
   * @param dimsSource	the dimensions of the source matrix
   * @param openSource	the indices of the "open" dimensions
   * @param target	the target matrix
   * @param indexTarget the target index template to use, initializes with all 0s if null
   * @param dimsTarget	the dimensions of the target matrix
   * @param openTarget 	the indices of the "open" dimensions
   * @param type	the element type to use for transferring the data
   */
  public static void transfer(Matrix source, int[] indexSource, int[] dimsSource, int[] openSource, Matrix target, int[] indexTarget, int[] dimsTarget, int[] openTarget, ArrayElementType type) {
    boolean	finished;

    if (indexSource == null)
      indexSource = new int[dimsSource.length];
    if (indexTarget == null)
      indexTarget = new int[dimsTarget.length];

    finished = false;
    while (!finished) {
      transfer(source, indexSource, target, indexTarget, type);
      finished = increment(indexSource, dimsSource, openSource);
      increment(indexTarget, dimsTarget, openTarget);
    }
  }

  /**
   * Returns the element according to the specified type.
   *
   * @param source	the matrix to get the element from
   * @param index	the index of the element
   * @param type	the type of element
   * @return		the value
   */
  public static Object getElement(Matrix source, int[] index, ArrayElementType type) {
    switch (type) {
      case BOOLEAN:
	return source.getBoolean(index);
      case BYTE:
	return source.getByte(index);
      case SHORT:
	return source.getShort(index);
      case INTEGER:
	return source.getInt(index);
      case LONG:
	return source.getLong(index);
      case FLOAT:
	return source.getFloat(index);
      case DOUBLE:
	return source.getDouble(index);
      default:
        throw new IllegalStateException("Unhandled element type: " + type);
    }
  }

  /**
   * Sets the element according to the specified type.
   *
   * @param target	the matrix to set the element in
   * @param index	the index of the element
   * @param type	the type of element
   * @param value 	the value of the element, gets automatically parsed if string
   * @return		null if successfully set, otherwise error message
   */
  public static String setElement(Matrix target, int[] index, ArrayElementType type, Object value) {
    String	result;

    result = null;
    try {
      // parse?
      if (value instanceof String) {
	switch (type) {
	  case BOOLEAN:
	    value = Boolean.parseBoolean((String) value);
	    break;
	  case BYTE:
	    value = Byte.parseByte((String) value);
	    break;
	  case SHORT:
	    value = Short.parseShort((String) value);
	    break;
	  case INTEGER:
	    value = Integer.parseInt((String) value);
	    break;
	  case LONG:
	    value = Long.parseLong((String) value);
	    break;
	  case FLOAT:
	    value = Float.parseFloat((String) value);
	    break;
	  case DOUBLE:
	    value = Double.parseDouble((String) value);
	    break;
	  default:
	    throw new IllegalStateException("Unhandled element type: " + type);
	}
      }

      switch (type) {
	case BOOLEAN:
	  target.setBoolean(index, (Boolean) value);
	  break;
	case BYTE:
	  target.setByte(index, (Byte) value);
	  break;
	case SHORT:
	  target.setShort(index, (Short) value);
	  break;
	case INTEGER:
	  target.setInt(index, (Integer) value);
	  break;
	case LONG:
	  target.setLong(index, (Long) value);
	  break;
	case FLOAT:
	  target.setFloat(index, (Float) value);
	  break;
	case DOUBLE:
	  target.setDouble(index, (Double) value);
	  break;
	default:
	  throw new IllegalStateException("Unhandled element type: " + type);
      }
    }
    catch (Exception e) {
      result = "Failed to set ";
    }

    return result;
  }

  /**
   * Generates a string representation of the array dimensions, uses 'x' as separator.
   *
   * @param array	the array to generate the dimensions for
   * @return		the generated string
   */
  public static String arrayDimensionsToString(Array array) {
    return arrayDimensionsToString(array, "x");
  }

  /**
   * Generates a string representation of the array dimensions.
   *
   * @param array	the array to generate the dimensions for
   * @param separator 	the separator to use for the dimensions
   * @return		the generated string
   */
  public static String arrayDimensionsToString(Array array, String separator) {
    StringBuilder 	result;

    result = new StringBuilder();
    for (int dim: array.getDimensions()) {
      if (result.length() > 0)
	result.append(separator);
      result.append(dim);
    }

    return result.toString();
  }
}
