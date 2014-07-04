/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;
import java.text.MessageFormat;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * L�schen einer Eigenschaft.
 */
public class EigenschaftDeleteAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context instanceof TablePart)
    {
      TablePart tp = (TablePart) context;
      context = tp.getSelection();
    }
    if (context == null || !(context instanceof Eigenschaft))
    {
      throw new ApplicationException("Keine Eigenschaft ausgew�hlt");
    }
    try
    {
      Eigenschaft ei = (Eigenschaft) context;
      if (ei.isNewObject())
      {
        return;
      }

      SimpleDialog d = new SimpleDialog(SimpleDialog.POSITION_CENTER);
      d.setTitle("Eigenschaft l�schen");
      DBIterator it = Einstellungen.getDBService().createList(
          Eigenschaften.class);
      it.addFilter("eigenschaft = ?", new Object[] { ei.getID() });
      d.setText(MessageFormat
          .format(
              "Die Eigenschaft kann nicht gel�scht werden. Sie ist noch mit {0} Mitglied(ern) verkn�pft.",
              it.size()));
      try
      {
        if (it.size() > 0)
        {
          d.open();
          return;
        }
      }
      catch (Exception e)
      {
        Logger.error("Fehler beim L�schen der Eigenschaft", e);
        return;
      }

      ei.delete();
      GUI.getStatusBar().setSuccessText("Eigenschaft gel�scht.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim L�schen der Eigenschaft";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
