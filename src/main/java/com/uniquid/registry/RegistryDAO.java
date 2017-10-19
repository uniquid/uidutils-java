package com.uniquid.registry;

import com.uniquid.registry.exception.RegistryException;

/**
 * Data Access Object interface for Registry
 */
public interface RegistryDAO {
	
	/**
	 * Insert a mapping between a provider name and an address
	 * 
	 * @param providerName the name of the provider
	 * @param providerAddress the address of the provider
	 * @throws RegistryException in case a problem occurs
	 */
	public void insertMapping(String providerName, String providerAddress) throws RegistryException;
	
	/**
	 * Returns the provider name from its address
	 * 
	 * @param providerAddress the provider address to lookup
	 * @return the provider name associated to the address
	 * @throws RegistryException in case a problem occurs
	 */
	public String retrieveProviderName(String providerAddress) throws RegistryException;

}
