package com.uniquid.blockchain.impl;

import com.uniquid.blockchain.AddressInfo;
import com.uniquid.blockchain.BlockChainDAO;
import com.uniquid.blockchain.Transaction;
import com.uniquid.blockchain.Utxo;
import com.uniquid.blockchain.exception.BlockChainException;
import com.uniquid.utils.DataProvider;
import com.uniquid.utils.HttpUtils;
import com.uniquid.utils.ResponseDecoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public class InsightApiDAOImpl implements BlockChainDAO {

	private static Logger LOGGER = LoggerFactory.getLogger(InsightApiDAOImpl.class);

	private static final String ADDR_URL = "%1&s/addr/%2&s";
	private static final String UTXOS_URL = "%1&s/addr/%2&s/utxo";
	private static final String RAWTX_URL = "%1&s/rawtx/%2&s";
	private static final String TRANSACTION_URL = "%1&s/tx/%2&s";
	private static final String SENDTX_URL = "%1&s/tx/send";
	
	private String insightApiHost;

	public InsightApiDAOImpl(String insightApiHost) {
		this.insightApiHost = insightApiHost;
	}

	@Override
	public AddressInfo retrieveAddressInfo(String address) throws BlockChainException {

		try {
			URL url = new URL(ADDR_URL.replace("%1&s", insightApiHost).replace("%2&s", address));

			return HttpUtils.retrieveDataViaHttpGet(url, new ResponseDecoder<AddressInfo>() {

				@Override
				public AddressInfo manageResponse(String serverResponse) {
					
					return addressFromJsonString(serverResponse);
					
				}

				@Override
				public int getExpectedResponseCode() {
					return HttpURLConnection.HTTP_OK;
				}

				@Override
				public AddressInfo manageUnexpectedResponseCode(int responseCode, String responseMessage)
						throws Exception {
					
					if (HttpURLConnection.HTTP_NOT_FOUND == responseCode)
						
						return null;
					
					throw new Exception("Received " + responseCode + responseMessage);
				}
				
			});

		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}

	}

	private static AddressInfo addressFromJsonString(String string) throws JSONException {

		AddressInfo addressInfo = new AddressInfo();

		JSONObject jsonMessage = new JSONObject(string);

		addressInfo.setBalance(jsonMessage.getDouble("balance"));
		addressInfo.setTotalReceived(jsonMessage.getDouble("totalReceived"));
		addressInfo.setTotalSent(jsonMessage.getDouble("totalSent"));
		addressInfo.setUnconfirmedBalance(jsonMessage.getDouble("unconfirmedBalance"));

		return addressInfo;

	}

	private static Collection<Utxo> utxosFromJsonString(String string, int maxUtxos) throws JSONException {

		Collection<Utxo> collection = new ArrayList<>();

		JSONArray jsonMessage = new JSONArray(string);

		int elements = jsonMessage.length();
		
		elements = (elements <= maxUtxos) ? elements : maxUtxos;

		for (int i = 0; i < elements; i++) {

			Utxo utxo = new Utxo();

			JSONObject object = jsonMessage.getJSONObject(i);

			utxo.setAddress(object.getString("address"));
			utxo.setTxid(object.getString("txid"));
			utxo.setVout(object.getLong("vout"));
			utxo.setScriptPubKey(object.getString("scriptPubKey"));
			utxo.setAmount(object.getDouble("amount"));
			utxo.setConfirmation(object.getLong("confirmations"));

			collection.add(utxo);
		}

		return collection;

	}
	
	private static Collection<Utxo> utxosFromJsonString(String string) throws JSONException {

		Collection<Utxo> collection = new ArrayList<>();

		JSONArray jsonMessage = new JSONArray(string);

		int elements = jsonMessage.length();
		
		for (int i = 0; i < elements; i++) {

			Utxo utxo = new Utxo();

			JSONObject object = jsonMessage.getJSONObject(i);

			utxo.setAddress(object.getString("address"));
			utxo.setTxid(object.getString("txid"));
			utxo.setVout(object.getLong("vout"));
			utxo.setScriptPubKey(object.getString("scriptPubKey"));
			utxo.setAmount(object.getDouble("amount"));
			utxo.setConfirmation(object.getLong("confirmations"));

			collection.add(utxo);
		}

		return collection;

	}

	@Override
	public Collection<Utxo> retrieveUtxo(String address) throws BlockChainException {

		try {

			URL url = new URL(UTXOS_URL.replace("%1&s", insightApiHost).replace("%2&s", address));

			return HttpUtils.retrieveDataViaHttpGet(url, new ResponseDecoder<Collection<Utxo>>() {

				@Override
				public Collection<Utxo> manageResponse(String serverResponse) {
					
					return utxosFromJsonString(serverResponse);
					
				}

				@Override
				public int getExpectedResponseCode() {
					return HttpURLConnection.HTTP_OK;
				}

				@Override
				public Collection<Utxo> manageUnexpectedResponseCode(int responseCode, String responseMessage)
						throws Exception {
					if (HttpURLConnection.HTTP_NOT_FOUND == responseCode)
						
						return new ArrayList<>();
					
					throw new Exception("Received " + responseCode + responseMessage);
				}
				
			});

		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}
	}
	
	@Override
	public Collection<Utxo> retrieveUtxo(String address, final int maxUtxo) throws BlockChainException {
		
		try {

			URL url = new URL(UTXOS_URL.replace("%1&s", insightApiHost).replace("%2&s", address));

			return HttpUtils.retrieveDataViaHttpGet(url, new ResponseDecoder<Collection<Utxo>>() {

				@Override
				public Collection<Utxo> manageResponse(String serverResponse) {
					return utxosFromJsonString(serverResponse, maxUtxo);
				}
				
				@Override
				public int getExpectedResponseCode() {
					return HttpURLConnection.HTTP_OK;
				}

				@Override
				public Collection<Utxo> manageUnexpectedResponseCode(int responseCode, String responseMessage)
						throws Exception {
					if (HttpURLConnection.HTTP_NOT_FOUND == responseCode)
						
						return new ArrayList<>();
					
					throw new Exception("Received " + responseCode + responseMessage);
				}

			});

		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}
	}

	private static String rawtxFromJsonString(String string) throws JSONException {
		JSONObject jsonMessage = new JSONObject(string);
		return jsonMessage.getString("rawtx");
	}

	@Override
	public String retrieveRawTx(String txid) throws BlockChainException {

		try {
			URL url = new URL(RAWTX_URL.replace("%1&s", insightApiHost).replace("%2&s", txid));

			return HttpUtils.retrieveDataViaHttpGet(url, new ResponseDecoder<String>() {

				@Override
				public String manageResponse(String serverResponse) {
					return rawtxFromJsonString(serverResponse);
				}
				
				@Override
				public int getExpectedResponseCode() {
					return HttpURLConnection.HTTP_OK;
				}

				@Override
				public String manageUnexpectedResponseCode(int responseCode, String responseMessage) throws Exception {
					if (HttpURLConnection.HTTP_NOT_FOUND == responseCode)
						
						return null;
					
					throw new Exception("Received " + responseCode + responseMessage);
				}

			});
			
		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}
	}
	
	private static Transaction transactionFromJsonString(String string) throws JSONException {

		JSONObject jsonMessage = new JSONObject(string);

		String txid = jsonMessage.getString("txid");
		
		long version = jsonMessage.getLong("version");
		
		long confirmations = jsonMessage.getLong("confirmations");
		
		long time = jsonMessage.getLong("time");
		
		String spentTxId = null;
		
		JSONArray vouts = jsonMessage.getJSONArray("vout");
		
		// avoid use of iterator because on Android we don't have this method!
		for (int i = 0; i < vouts.length(); i++) {
			
			JSONObject vout = (JSONObject) vouts.get(i);
			
			if (vout.getLong("n") == 2) {
				
				if (!vout.isNull("spentTxId")) {
					
					spentTxId = vout.getString("spentTxId");

				}
				
			}
			
		}
		
		Transaction transaction = new Transaction();
		
		transaction.setTxid(txid);
		transaction.setVersion(version);
		transaction.setConfirmations(confirmations);
		transaction.setTime(time);
		transaction.setSpentTxId(spentTxId);
		
		return transaction;

	}
	
	@Override
	public Transaction retrieveTransaction(String txid) throws BlockChainException {
		
		try {
			URL url = new URL(TRANSACTION_URL.replace("%1&s", insightApiHost).replace("%2&s", txid));
			
			return HttpUtils.retrieveDataViaHttpGet(url, new ResponseDecoder<Transaction>() {

				@Override
				public Transaction manageResponse(String serverResponse) {
					return transactionFromJsonString(serverResponse);
				}
				
				@Override
				public int getExpectedResponseCode() {
					return HttpURLConnection.HTTP_OK;
				}

				@Override
				public Transaction manageUnexpectedResponseCode(int responseCode, String responseMessage)
						throws Exception {
					
					if (HttpURLConnection.HTTP_NOT_FOUND == responseCode)
						
						return null;
					
					throw new Exception("Received " + responseCode + responseMessage);
					
				}

			});

		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}
	}
	
	private static String txidFromJsonString(String string) throws JSONException {
		JSONObject jsonMessage = new JSONObject(string);
		return jsonMessage.getString("txid");
	}

	@Override
	public String sendTx(String rawtx) throws BlockChainException {
		
		try {
			
			JSONObject jsonMessage = new JSONObject();

			jsonMessage.put("rawtx", rawtx);
			
			final byte[] postDataBytes = jsonMessage.toString().getBytes(StandardCharsets.UTF_8);

			URL url = new URL(SENDTX_URL.replace("%1&s", insightApiHost));
			
			return HttpUtils.sendDataWithPost(url, new DataProvider<String>() {

				@Override
				public String manageResponse(String serverResponse) {
					return txidFromJsonString(serverResponse);
				}
				
				@Override
				public String getContentType() {
					return "application/json";
				}

				@Override
				public String getCharset() {
					return "utf-8";
				}

				@Override
				public byte[] getPayload() {
					return postDataBytes;
				}

				@Override
				public int getExpectedResponseCode() {
					return HttpURLConnection.HTTP_OK;
				}

				@Override
				public String manageUnexpectedResponseCode(int responseCode, String responseMessage) throws Exception {
					
					if (HttpURLConnection.HTTP_NOT_FOUND == responseCode)
						
						return null;
					
					throw new Exception("Received " + responseCode + responseMessage);
					
				}
				
			});
			

		} catch (Throwable t) {
			
			throw new BlockChainException("Unexpected Exception", t);
			
		}
	}

}
