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
 * ClassificationModel.java
 * Copyright (C) 2016-2018 University of Waikato, Hamilton, NZ
 */

package adams.ml.model.classification;

import adams.data.spreadsheet.Row;
import adams.ml.model.Model;

/**
 * Interface for classification models.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface ClassificationModel
  extends Model {

  /**
   * Returns the label for the given row.
   *
   * @param row		the row to classify
   * @return		the class label
   * @throws Exception	if prediction fails
   */
  public String classify(Row row) throws Exception;

  /**
   * Returns the class distribution for the given row.
   *
   * @param row		the row to generate the class distribution for
   * @return		the class distribution
   * @throws Exception	if prediction fails
   */
  public double[] distribution(Row row) throws Exception;
}
