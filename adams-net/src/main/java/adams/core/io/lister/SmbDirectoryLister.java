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

/**
 * SmbDirectoryLister.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package adams.core.io.lister;

import adams.core.base.BasePassword;
import adams.core.io.FileObject;
import adams.core.io.SmbFileObject;
import adams.core.net.SMBAuthenticationProvider;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * Lists files/dirs on a remote server using SMB.
 * The authentication object takes precedence over domain/user/password.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class SmbDirectoryLister
  extends AbstractRecursiveDirectoryLister {

  private static final long serialVersionUID = 2687222234652386893L;

  /** the SMB host. */
  protected String m_Host;

  /** the SMB domain. */
  protected String m_Domain;

  /** the SMB user to use. */
  protected String m_User;

  /** the SMB password to use. */
  protected BasePassword m_Password;

  /** the authentication provider to use. */
  protected SMBAuthenticationProvider m_AuthenticationProvider;

  /** the authentication to use. */
  protected transient NtlmPasswordAuthentication m_Authentication;

  /**
   * Sets the host to connect to.
   *
   * @param value	the host name/ip
   */
  public void setHost(String value) {
    m_Host = value;
  }

  /**
   * Returns the host to connect to.
   *
   * @return		the host name/ip
   */
  public String getHost() {
    return m_Host;
  }

  /**
   * Sets the domain to use.
   *
   * @param value	the domain
   */
  public void setDomain(String value) {
    m_Domain = value;
  }

  /**
   * Returns the domain to use.
   *
   * @return 		the domain
   */
  public String getDomain() {
    return m_Domain;
  }

  /**
   * Sets the SMB user to use.
   *
   * @param value	the user name
   */
  public void setUser(String value) {
    m_User = value;
  }

  /**
   * Returns the SMB user name to use.
   *
   * @return		the user name
   */
  public String getUser() {
    return m_User;
  }

  /**
   * Sets the SMB password to use.
   *
   * @param value	the password
   */
  public void setPassword(BasePassword value) {
    m_Password = value;
  }

  /**
   * Returns the SMB password to use.
   *
   * @return		the password
   */
  public BasePassword getPassword() {
    return m_Password;
  }

  /**
   * Sets the authentication provider to use.
   *
   * @param value	the provider
   */
  public void setAuthenticationProvider(SMBAuthenticationProvider value) {
    m_AuthenticationProvider = value;
  }

  /**
   * Returns the authentication provider to use.
   *
   * @return		the provider
   */
  public SMBAuthenticationProvider getAuthenticationProvider() {
    return m_AuthenticationProvider;
  }

  /**
   * Returns whether the directory lister operates locally or remotely.
   *
   * @return		true if local lister
   */
  public boolean isLocal() {
    return false;
  }

  /**
   * Returns whether the watch directory has a parent directory.
   *
   * @return		true if parent directory available
   */
  public boolean hasParentDirectory() {
    return (new File(m_WatchDir).getParentFile() != null);
  }

  /**
   * Returns a new directory relative to the watch directory.
   *
   * @param dir		the directory name
   * @return		the new wrapper
   */
  public SmbFileObject newDirectory(String dir) {
    return newDirectory(m_WatchDir, dir);
  }

  /**
   * Returns a new directory relative to the watch directory.
   *
   * @param dir		the directory name
   * @return		the new wrapper
   */
  public SmbFileObject newDirectory(String parent, String dir) {
    String 	pdir;
    try {
      pdir = parent;
      if (!pdir.startsWith("/"))
	pdir = "/" + pdir;
      if (!pdir.endsWith("/"))
	pdir += "/";
      if (!dir.endsWith("/"))
	dir += dir;
      return new SmbFileObject(new SmbFile("smb://" + m_Host + pdir + dir));
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Performs the recursive search. Search goes deeper if != 0 (use -1 to
   * start with for infinite search).
   *
   * @param context	the context to use
   * @param files	the files collected so far
   * @param depth	the depth indicator (searched no deeper, if 0)
   * @throws Exception	if listing fails
   */
  protected void search(SmbFile context, List<SortContainer> files, int depth) throws Exception {
    SmbFile[]	currFiles;
    SmbFile	entry;
    int		i;

    if (depth == 0)
      return;

    if (getDebug())
      getLogger().info("search: context=" + context + ", depth=" + depth);

    currFiles = context.listFiles();
    if (currFiles == null) {
      if (getDebug())
	getLogger().info("No files listed!");
      return;
    }

    for (i = 0; i < currFiles.length; i++) {
      // do we have to stop?
      if (m_Stopped)
	break;

      entry = currFiles[i];

      // directory?
      if (entry.isDirectory()) {
	// ignore "." and ".."
	if (entry.getName().equals(".") || entry.getName().equals(".."))
	  continue;

	// search recursively?
	if (m_Recursive)
	  search(entry, files, depth - 1);

	if (m_ListDirs) {
	  // does name match?
	  if (!m_RegExp.isEmpty() && !m_RegExp.isMatch(entry.getName()))
	    continue;

	  files.add(new SortContainer(new SmbFileObject(entry), m_Sorting));
	}
      }
      else {
	if (m_ListFiles) {
	  // does name match?
	  if (!m_RegExp.isEmpty() && !m_RegExp.isMatch(entry.getName()))
	    continue;

	  files.add(new SortContainer(new SmbFileObject(entry), m_Sorting));
	}
      }
    }
  }

  /**
   * Returns the list of files/directories in the watched directory. In case
   * the execution gets stopped, this method returns empty list.
   *
   * @param context	the context
   * @return		the list of absolute file/directory names
   * @throws Exception	if listing fails
   */
  public List<SmbFileObject> search(SmbFile context) throws Exception {
    List<SmbFileObject>		result;
    List<SortContainer>		list;
    SortContainer		cont;
    int				i;

    result    = new ArrayList<>();
    m_Stopped = false;

    if (m_ListFiles || m_ListDirs) {
      if (getDebug())
	getLogger().info("watching '" + m_WatchDir + "'");

      if (getDebug())
	getLogger().info("before search(...)");
      list = new ArrayList<>();
      search(context, list, m_MaxDepth);

      // sort files ascendingly regarding lastModified
      if (getDebug())
	getLogger().info("before obtaining last modified timestamps");

      if (!m_Stopped && (m_Sorting != Sorting.NO_SORTING)) {
	if (getDebug())
	  getLogger().info("before sorting");
	Collections.sort(list);
	if (m_SortDescending) {
	  for (i = 0; i < list.size() / 2; i++) {
	    cont = list.get(i);
	    list.set(i, list.get(list.size() - 1 - i));
	    list.set(list.size() - 1 - i, cont);
	  }
	}
      }

      // match filenames and them to the result
      if (!m_Stopped) {
	if (getDebug())
	  getLogger().info("before matching");
	for (i = 0; i < list.size(); i++) {
	  result.add((SmbFileObject) list.get(i).getFile());

	  // maximum reached?
	  if (m_MaxItems > 0) {
	    if (result.size() == m_MaxItems) {
	      if (getDebug())
		getLogger().info("max size reached");
	      break;
	    }
	  }

	  // do we have to stop?
	  if (m_Stopped)
	    break;
	}
      }
    }

    // do we have to stop?
    if (m_Stopped)
      result.clear();

    return result;
  }

  /**
   * Returns the list of files/directories in the watched directory. In case
   * the execution gets stopped, this method returns a 0-length array.
   *
   * @return		 the list of absolute file/directory names
   */
  @Override
  public String[] list() {
    String[]		result;
    FileObject[]	wrappers;
    int			i;

    wrappers = listObjects();
    result   = new String[wrappers.length];
    for (i = 0; i < wrappers.length; i++)
      result[i] = wrappers[i].toString();

    return result;
  }

  /**
   * Returns the list of files/directories in the watched directory. In case
   * the execution gets stopped, this method returns a 0-length array.
   *
   * @return		 the list of file/directory wrappers
   */
  public SmbFileObject[] listObjects() {
    List<SmbFileObject> 	result;
    SmbFile			context;
    String			dir;

    result    = new ArrayList<>();
    m_Stopped = false;
    if (m_Authentication == null) {
      if (m_AuthenticationProvider != null)
	m_Authentication = m_AuthenticationProvider.getAuthentication();
      else
	m_Authentication = new NtlmPasswordAuthentication(m_Domain, m_User, m_Password.getValue());
    }

    try {
      dir = m_WatchDir;
      if (!dir.startsWith("/"))
	dir = "/" + dir;
      if (!dir.endsWith("/"))
	dir += "/";
      context = new SmbFile("smb://" + m_Host + dir, m_Authentication);
      result.addAll(search(context));
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to create search context!", e);
    }

    return result.toArray(new SmbFileObject[result.size()]);
  }
}
