package com.uniquid.blockchain.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uniquid.blockchain.AddressInfo;
import com.uniquid.blockchain.BlockChainDAO;
import com.uniquid.blockchain.Utxo;
import com.uniquid.blockchain.exception.BlockChainException;

public class InsightApiDAOImpl implements BlockChainDAO {

	private static Logger LOGGER = LoggerFactory.getLogger(InsightApiDAOImpl.class);

	private static final String ADDR_URL = "%1&s/insight-api/addr/%2&s";
	private static final String UTXOS_URL = "%1&s/insight-api/addr/%2&s/utxo";
	private static final String RAWTX_URL = "%1&s/insight-api/rawtx/%2&s";
	private static final String SENDTX_URL = "%1&s/insight-api/tx/send";

	private String insightApiHost;

	public InsightApiDAOImpl(String insightApiHost) {
		this.insightApiHost = insightApiHost;
	}

	@Override
	public AddressInfo retrieveAddressInfo(String address) throws BlockChainException {

		try {
			URL url = new URL(ADDR_URL.replace("%1&s", insightApiHost).replace("%2&s", address));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// optional default is GET
			connection.setRequestMethod("GET");

			// add request header
			connection.setRequestProperty("User-Agent", "UNIQUID-UTILS-0.1");

			if (200 == connection.getResponseCode()) {

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				in.close();

				return addressFromJsonString(response.toString());

			} else {

				return null;

			}

		} catch (MalformedURLException ex) {

			throw new BlockChainException("Exception Accessing URL", ex);

		} catch (IOException ex) {

			throw new BlockChainException("Exception while I/O", ex);

		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}

	}

	private static AddressInfo addressFromJsonString(String string) throws JSONException {

		AddressInfo addressInfo = new AddressInfo();

		JSONObject jsonMessage = new JSONObject(string);

		addressInfo.setBalance(jsonMessage.getLong("balanceSat"));
		addressInfo.setTotalReceived(jsonMessage.getLong("totalReceivedSat"));
		addressInfo.setTotalSent(jsonMessage.getLong("totalSentSat"));
		addressInfo.setUnconfirmedBalance(jsonMessage.getLong("unconfirmedBalanceSat"));

		return addressInfo;

	}

	private static Collection<Utxo> utxosFromJsonString(String string) throws JSONException {

		Collection<Utxo> collection = new ArrayList<Utxo>();

		JSONArray jsonMessage = new JSONArray(string);

		int elements = jsonMessage.length();

		for (int i = 0; i < elements; i++) {

			Utxo utxo = new Utxo();

			JSONObject object = jsonMessage.getJSONObject(i);

			utxo.setAddress(object.getString("address"));
			utxo.setTxid(object.getString("txid"));
			utxo.setVout(object.getLong("vout"));
			utxo.setScriptPubKey(object.getString("scriptPubKey"));
			utxo.setAmount(object.getLong("satoshis"));
			utxo.setConfirmation(object.getLong("confirmations"));

			collection.add(utxo);
		}

		return collection;

	}

	@Override
	public Collection<Utxo> retrieveUtxo(String address) throws BlockChainException {

		try {

			URL url = new URL(UTXOS_URL.replace("%1&s", insightApiHost).replace("%2&s", address));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// optional default is GET
			connection.setRequestMethod("GET");

			// add request header
			connection.setRequestProperty("User-Agent", "UNIQUID-UTILS-0.1");

			if (200 == connection.getResponseCode()) {

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				in.close();
				
				return utxosFromJsonString(response.toString());

			} else {

				return new ArrayList<Utxo>();

			}

		} catch (MalformedURLException ex) {

			throw new BlockChainException("Exception Accessing URL", ex);

		} catch (IOException ex) {

			throw new BlockChainException("Exception while I/O", ex);

		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}
	}

	private static String rawtxFromJsonString(String string) throws JSONException {

		JSONObject jsonMessage = new JSONObject(string);

		String rawtx = jsonMessage.getString("rawtx");

		return rawtx;

	}

	@Override
	public String retrieveRawTx(String txid) throws BlockChainException {

		try {
			URL url = new URL(RAWTX_URL.replace("%1&s", insightApiHost).replace("%2&s", txid));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// optional default is GET
			connection.setRequestMethod("GET");

			// add request header
			connection.setRequestProperty("User-Agent", "UNIQUID-UTILS-0.1");

			if (200 == connection.getResponseCode()) {

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				in.close();

				return rawtxFromJsonString(response.toString());

			} else {

				return null;

			}

		} catch (MalformedURLException ex) {

			throw new BlockChainException("Exception Accessing URL", ex);

		} catch (IOException ex) {

			throw new BlockChainException("Exception while I/O", ex);

		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}
	}
	
	private static String txidFromJsonString(String string) throws JSONException {

		JSONObject jsonMessage = new JSONObject(string);

		String txid = jsonMessage.getString("txid");

		return txid;

	}

	@Override
	public String sendTx(String rawtx) throws BlockChainException {
		
		try {
			
			JSONObject jsonMessage = new JSONObject();

			jsonMessage.put("rawtx", rawtx);
			
			byte[] postDataBytes = jsonMessage.toString().getBytes("UTF-8");

			URL url = new URL(SENDTX_URL.replace("%1&s", insightApiHost));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// optional default is GET
			connection.setRequestMethod("POST");

			// add request header
			connection.setRequestProperty("User-Agent", "UNIQUID-UTILS-0.1");
			
			connection.setRequestProperty("Content-Type", "application/json");
			
			connection.setRequestProperty("Charset", "utf-8");
			
			connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			
			connection.setDoOutput(true);
			
			connection.getOutputStream().write(postDataBytes);

			if (200 == connection.getResponseCode()) {

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				in.close();

				return txidFromJsonString(response.toString());

			} else {
				
				throw new BlockChainException("Error while submitting tx: " + connection.getResponseCode() + " " +
						connection.getResponseMessage());

			}

		} catch (MalformedURLException ex) {

			throw new BlockChainException("Exception Accessing URL", ex);

		} catch (IOException ex) {

			throw new BlockChainException("Exception while I/O", ex);

		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}
	}

}
