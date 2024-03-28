/**********************************************************************
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
package de.jost_net.JVerein.gui.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.action.BuchungImportAction;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.control.BuchungsHeaderControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.util.ApplicationException;

public class BuchungslisteView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Buchungen");
    
    final BuchungsControl control = new BuchungsControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Konto");
    group.addLabelPair("Konto", control.getSuchKonto());

    TabFolder folder = new TabFolder(getParent(), SWT.V_SCROLL | SWT.BORDER);
    folder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    // Erster Tab
    TabGroup tabAllgemein = new TabGroup(folder, "Suche Buchungen", true, 2);
    LabelGroup labelgroup1 = new LabelGroup(tabAllgemein.getComposite(), "Filter");
    ColumnLayout cl = new ColumnLayout(labelgroup1.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cl.getComposite());
    SimpleContainer right = new SimpleContainer(cl.getComposite());
    left.addLabelPair("Buchungsart", control.getSuchBuchungsart());
    left.addLabelPair("Projekt", control.getSuchProjekt());
    left.addLabelPair("Betrag", control.getSuchBetrag());
    left.addLabelPair("Mitglied zugeordnet?", control.getSuchMitgliedZugeordnet());
    right.addLabelPair("Von Datum", control.getVondatum());
    right.addLabelPair("Bis Datum", control.getBisdatum());
    right.addLabelPair("Enthaltener Text", control.getSuchtext());
    
    ButtonArea buttons1 = new ButtonArea();
    Button suchen = new Button("Suchen", new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        control.refreshBuchungsList();
      }
    }, null, true, "search.png");
    buttons1.addButton(suchen);
    labelgroup1.addButtonArea(buttons1);

    // Zweiter Tab
    final BuchungsHeaderControl headerControl = new BuchungsHeaderControl(
        this, control);
    TabGroup tabKonto = new TabGroup(folder, "Konto Kenndaten", true, 4);
    LabelGroup labelgroup2 = new LabelGroup(tabKonto.getComposite(), "");
    ColumnLayout c2 = new ColumnLayout(labelgroup2.getComposite(), 2);
    SimpleContainer left2 = new SimpleContainer(c2.getComposite());
    SimpleContainer right2 = new SimpleContainer(c2.getComposite());
    left2.addLabelPair("Konto:", headerControl.getKontoNameInput());
    right2.addLabelPair("Vorjahr", new LabelInput(""));
    left2.addLabelPair("Anfangssaldo:",
        headerControl.getAktJahrAnfangsSaldoInput());
    right2.addLabelPair("Anfangssaldo:",
        headerControl.getVorJahrAnfangsSaldoInput());
    left2.addLabelPair("Einnahmen:",
        headerControl.getAktJahrEinnahmenInput());
    right2.addLabelPair("Einnahmen:",
        headerControl.getVorJahrEinnahmenInput());
    left2.addLabelPair("Ausgaben:",
        headerControl.getAktJahrAusgabenInput());
    right2.addLabelPair("Ausgaben:",
        headerControl.getVorJahrAusgabenInput());
    left2.addLabelPair("Saldo:", headerControl.getAktJahrSaldoInput());
    right2.addLabelPair("Saldo:", headerControl.getVorJahrSaldoInput());

    control.getBuchungsList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BUCHUNGEN, false, "question-circle.png");
    buttons.addButton("Import", new BuchungImportAction(), null, false,
        "file-import.png");
    buttons.addButton(control.getStartCSVAuswertungButton());
    buttons.addButton(control.getStartAuswertungBuchungsjournalButton());
    buttons.addButton(control.getStartAuswertungEinzelbuchungenButton());
    buttons.addButton(control.getStartAuswertungSummenButton());
    buttons.addButton(control.getStarteBuchungMitgliedskontoZuordnungAutomatischButton());
    buttons.addButton("Neu", new BuchungNeuAction(), control, false,
        "document-new.png");
    buttons.paint(this.getParent());
  }
}
