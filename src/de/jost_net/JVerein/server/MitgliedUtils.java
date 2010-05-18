package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBIterator;

public class MitgliedUtils
{
  public static void setNurAktive(DBIterator it, Date datum)
      throws RemoteException
  {
    it.addFilter("(austritt is null or austritt < ?)", new Object[] { datum });
  }

  public static void setNurAktive(DBIterator it) throws RemoteException
  {
    setNurAktive(it, new Date());
  }

}
