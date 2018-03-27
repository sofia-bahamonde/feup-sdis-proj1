package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote {
    void backup(String file_path,int rep_degree) throws RemoteException;
}