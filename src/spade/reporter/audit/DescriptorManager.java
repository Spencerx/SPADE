/*
 --------------------------------------------------------------------------------
 SPADE - Support for Provenance Auditing in Distributed Environments.
 Copyright (C) 2015 SRI International

 This program is free software: you can redistribute it and/or
 modify it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 --------------------------------------------------------------------------------
 */
package spade.reporter.audit;

import java.util.HashMap;
import java.util.Map;

public class DescriptorManager {

	private Map<String, Map<String, ArtifactIdentity>> descriptors = new HashMap<>();
	
	public void addDescriptor(String pid, String fd, ArtifactIdentity artifactInfo){
		if(descriptors.get(pid) == null){
			descriptors.put(pid, new HashMap<String, ArtifactIdentity>());
		}
		descriptors.get(pid).put(fd, artifactInfo);
	}
	
	public void addDescriptors(String pid, Map<String, ArtifactIdentity> newDescriptors){
		if(descriptors.get(pid) == null){
			descriptors.put(pid, new HashMap<String, ArtifactIdentity>());
		}
		descriptors.get(pid).putAll(newDescriptors);
	}
	
	public ArtifactIdentity removeDescriptor(String pid, String fd){
		if(descriptors.get(pid) == null){
			return null;
		}
		return descriptors.get(pid).remove(fd);
	}
	
	public ArtifactIdentity getDescriptor(String pid, String fd){
		if(descriptors.get(pid) == null){
			descriptors.put(pid, new HashMap<String, ArtifactIdentity>());
		}
		if(descriptors.get(pid).get(fd) == null){
			String path = null;
			if("0".equals(fd)){
	    		path = "stdin";
	    	}else if("1".equals(fd)){
	    		path = "stdout";
	    	}else if("2".equals(fd)){
	    		path = "stderr";
	    	}
			if(path != null){
				descriptors.get(pid).put(fd, new FileIdentity(path));
			}
		}
		return descriptors.get(pid).get(fd);
	}
	
	public void copyDescriptors(String fromPid, String toPid){
		if(descriptors.get(fromPid) == null){
			return;
		}
		if(descriptors.get(toPid) == null){
			descriptors.put(toPid, new HashMap<String, ArtifactIdentity>());
		}
		descriptors.get(toPid).putAll(descriptors.get(fromPid));
	}
	
	public void linkDescriptors(String fromPid, String toPid){
		if(descriptors.get(fromPid) == null){
			descriptors.put(fromPid, new HashMap<String, ArtifactIdentity>());
		}
		descriptors.put(toPid, descriptors.get(fromPid));
	}
	
	public void unlinkDescriptors(String pid){
		if(descriptors.get(pid) == null){
			return;
		}
		descriptors.put(pid, new HashMap<String, ArtifactIdentity>(descriptors.get(pid)));
	}
	
	public void addUnknownDescriptor(String pid, String fd){
		addDescriptor(pid, fd, new UnknownIdentity(pid, fd));
	}
}
