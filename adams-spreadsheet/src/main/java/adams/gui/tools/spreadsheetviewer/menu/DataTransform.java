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
 * DataTransform.java
 * Copyright (C) 2014-2022 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.tools.spreadsheetviewer.menu;

import adams.data.spreadsheet.SpreadSheet;
import adams.flow.transformer.AbstractSpreadSheetTransformer;
import adams.flow.transformer.AbstractTransformer;
import adams.flow.transformer.SpreadSheetSubset;
import adams.gui.goe.GenericObjectEditorDialog;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;

/**
 * Filters the columns.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class DataTransform
  extends AbstractSpreadSheetViewerMenuItemAction {

  /** for serialization. */
  private static final long serialVersionUID = 5235570137451285010L;

  /** the last transformer. */
  protected AbstractSpreadSheetTransformer m_LastTransformer;

  /**
   * Returns the caption of this action.
   * 
   * @return		the caption, null if not applicable
   */
  @Override
  protected String getTitle() {
    return "Transform...";
  }

  /**
   * Creates a new dialog.
   * 
   * @return		the dialog
   */
  @Override
  protected GenericObjectEditorDialog createDialog() {
    GenericObjectEditorDialog	result;
    
    if (getParentDialog() != null)
      result = new GenericObjectEditorDialog(getParentDialog(), ModalityType.DOCUMENT_MODAL);
    else
      result = new GenericObjectEditorDialog(getParentFrame(), true);
    result.setTitle("Transformer");
    result.setUISettingsPrefix(AbstractSpreadSheetTransformer.class);
    result.getGOEEditor().setClassType(AbstractSpreadSheetTransformer.class);
    result.getGOEEditor().setCanChangeClassInDialog(true);
    result.setCurrent(m_LastTransformer);
    result.setLocationRelativeTo(m_State);
    
    return result;
  }
  
  /**
   * Invoked when an action occurs.
   */
  @Override
  protected void doActionPerformed(ActionEvent e) {
    SpreadSheet		sheet;
    AbstractTransformer	transformer;

    sheet = getTabbedPane().getCurrentSheet();
    if (sheet == null)
      return;

    if (m_LastTransformer == null)
      m_LastTransformer = new SpreadSheetSubset();

    getDialog().setVisible(true);
    if (getDialog().getResult() != GenericObjectEditorDialog.APPROVE_OPTION)
      return;

    transformer = (AbstractTransformer) getDialog().getGOEEditor().getValue();

    m_State.filterData(getTabbedPane().getTitleAt(getTabbedPane().getSelectedIndex()), sheet, transformer);
  }

  /**
   * Performs the actual update of the state of the action.
   */
  @Override
  protected void doUpdate() {
    setEnabled(isSheetSelected());
  }
}
