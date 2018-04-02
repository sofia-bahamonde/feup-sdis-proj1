package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote {
    void backup(String file_path,int rep_degree) throws RemoteException;

	void delete(String file_path) throws RemoteException;

	void restore(String file_path) throws RemoteException;

	void state()throws RemoteException;

	void raclaim(int space)throws RemoteException;

}