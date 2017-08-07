package com.uniquid.registry;

import com.uniquid.registry.exception.RegistryException;

public interface RegistryDAO {
	
	public void insertMapping(String providerName, String providerAddress) throws RegistryException;
	
	public String retrieveProviderName(String providerName) throws RegistryException;

}
