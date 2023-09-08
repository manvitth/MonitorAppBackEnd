package com.vis.monitor.service.impl;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vis.monitor.repository.WebServiceRepository;
import com.vis.monitor.service.WSService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.vis.monitor.ws.modal.Data;
import com.vis.monitor.ws.modal.KeyValue;
import com.vis.monitor.ws.modal.WebService;
import com.vis.monitor.ws.modal.WebServiceRequest;
import com.vis.monitor.ws.modal.WebServiceResponse;

import lombok.extern.log4j.Log4j2;

@SuppressWarnings("deprecation")
@Service
@Log4j2
public class WSServiceImpl implements WSService {

	@Autowired
	private WebServiceRepository wsRepo;

	@Override
	public WebService addWebService(WebService webService) {
		return wsRepo.save(webService);
	}

	@Override
	public WebService updateWebService(WebService webService) {
		return wsRepo.save(webService);
	}

	@Override
	public List<WebService> getWebServices() {
		return wsRepo.findAll();
	}

	@Override
	public WebService getWebService(Long id) {
		Optional<WebService> oWebService = wsRepo.findById(id);
		return oWebService != null && oWebService.isPresent() ? oWebService.get() : null;
	}

	@Override
	public WebService getWebService(String name) {
		Optional<WebService> oWebService = wsRepo.findByName(name);
		return oWebService != null && oWebService.isPresent() ? oWebService.get() : null;
	}

	@Override
	public WebService deleteWebService(Long id) {
		Optional<WebService> oWebService = wsRepo.findById(id);
		if (oWebService != null && oWebService.isPresent()) {
			wsRepo.deleteById(id);
		}
		return oWebService != null && oWebService.isPresent() ? oWebService.get() : null;

	}

	@Override
	public WebServiceResponse executeWebService(WebServiceRequest requestObj) {
		WebServiceResponse responseObj = new WebServiceResponse();

		try {

			Optional<WebService> oWebService = wsRepo.findByName(requestObj.getWsName());

			if (oWebService == null || !oWebService.isPresent()) {
				return responseObj;
			}

			WebService webService = oWebService.get();

			if(requestObj.getRequestData() != null) {
				webService.setRequestData(requestObj.getRequestData());
			}
			
			HttpClientBuilder builder = HttpClientBuilder.create();

			if (webService.getDisableCertificateAuthentication() != null
					&& webService.getDisableCertificateAuthentication()) {
				builder.setSSLContext(
						new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
						.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
			}

			int timeOut = webService.getTimeout() != null ? (webService.getTimeout() * 1000) : (15 * 1000);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeOut)
					.setConnectionRequestTimeout(timeOut).setSocketTimeout(timeOut).build();

			builder.setDefaultRequestConfig(requestConfig);

			HttpClient httpClient = builder.build();

			String url = webService.getUrl().trim();

			for (Data data : webService.getRequestData()) {
				if (data.getName() != null && data.getValue() != null && url.contains(data.getName().trim())) {
					url = url.replace(data.getName(), data.getValue());
				}
			}

			URI uri = new URIBuilder(url).build();

			RequestBuilder request = RequestBuilder.create(webService.getHttpMethod().name()).setUri(uri);

			if (webService.getHeaders() != null) {

				for (KeyValue header : webService.getHeaders()) {

					if ((header.getKey() != null && header.getKey().trim().equalsIgnoreCase("Authorization"))
							&& (header.getValue() != null && header.getValue().trim().contains("${TOKEN}"))) {
						Data data = getDataByName(webService.getRequestData(), "${TOKEN}");
						if (data != null) {
							request = request.addHeader(header.getKey(),
									header.getValue().replace("${TOKEN}", data.getValue()));
						}

					} else {
						request = request.addHeader(header.getKey(), header.getValue());
					}
				}

			}

			if (webService.getContentType() != null) {
				request = request.addHeader("Content-Type", webService.getContentType());
			}

			if (webService.getAccept() != null) {
				request = request.addHeader("Accept", webService.getAccept());
			}

			if (webService.getParameters() != null) {

				for (KeyValue param : webService.getParameters()) {
					boolean isAdded = false;
					for (Data data : webService.getRequestData()) {
						if (param.getValue() != null && data.getName() != null && data.getValue() != null
								&& param.getValue().trim().contains(data.getName().trim())) {

							String value = param.getValue().replace(data.getName(), data.getValue());
							request = request.addParameter(param.getKey(), value);
							isAdded = true;
						}
					}
					if (!isAdded) {
						request = request.addParameter(param.getKey(), param.getValue());
					}

				}

			}

			if (webService.getRequestBody() != null) {
				String entityStr = webService.getRequestBody();
				for (Data data : webService.getRequestData()) {
					if (data.getName() != null && data.getValue() != null
							&& entityStr.trim().contains(data.getName().trim())) {
						entityStr = entityStr.replace(data.getName(), data.getValue());
					} else if (data.getName() != null && data.getValue() == null
							&& entityStr.trim().contains(data.getName().trim())) {
						entityStr = entityStr.replace(data.getName(), "");
					}
				}

				request.setEntity(new StringEntity(entityStr, "UTF-8"));
				if (!webService.getIsPrivate()) {
					log.debug("Web Service | Execute WS | Request : " + entityStr);
				}
			}

			HttpUriRequest httpRequest = request.build();

			if (!webService.getIsPrivate()) {
				log.debug("Web Service | Execute WS | Http Complete Request : " + httpRequest);
				System.out.println("Http Complete Request : " + httpRequest);
			}

			HttpResponse httpResponse = httpClient.execute(httpRequest);

			responseObj.setResponseCode(httpResponse.getStatusLine().getStatusCode());

			if (httpResponse.getStatusLine().getStatusCode() >= 200
					&& httpResponse.getStatusLine().getStatusCode() < 300) {

				String responseStr = httpResponse.getEntity() != null
						? EntityUtils.toString(httpResponse.getEntity(), "UTF-8")
						: "";

				responseObj.setResponseString(responseStr);

				if (responseStr != null) {
					if (!webService.getIsPrivate()) {
						log.info("Web Service | Execute WS | IVR Web Service Response String : " + responseStr);
						System.out.println("IVR Web Service Response String : " + responseStr);
					}
					ContentType accept = webService.getAccept() == null ? ContentType.APPLICATION_JSON
							: ContentType.parse(webService.getAccept());

					List<Data> responseData = parseResponse(accept, responseStr, webService.getResponseData());

					responseObj.setResponseData(responseData);
					if (!webService.getIsPrivate()) {
						log.info("Web Service | Execute WS | IVR Web Service Response Data : " + responseObj);
						System.out.println("IVR Web Service Response Data : " + responseObj);
					}

				} else {
					log.info("Web Service | Execute WS | IVR Web Service No Response ");
					System.out.println("IVR Web Service No Response ");
				}

			} else {
				log.error("Web Service | Execute WS | Http Status Line : " + httpResponse.getStatusLine());
				String responseStr = httpResponse.getEntity() != null
						? EntityUtils.toString(httpResponse.getEntity(), "UTF-8")
						: "";
				responseObj.setResponseString(responseStr);
				log.error("Web Service | Execute WS | Http Response : " + responseStr);
				System.out.println(httpResponse.getStatusLine());
				System.out.println(EntityUtils.toString(httpResponse.getEntity()));
			}

		} catch (Exception ex) {
			log.error("Web Service | Execute WS | Exception : " + ex.getMessage(), ex);
		}

		return responseObj;
	}

	private Data getDataByName(List<Data> datas, String name) {
		Data data = null;

		try {

			data = datas.stream().filter(d -> d.getName().trim().equalsIgnoreCase(name.trim())).findFirst().get();

		} catch (Exception ex) {
			log.error("Web Service | Get Data By Name | Exception : " + ex.getMessage(), ex);
		}

		return data;
	}

	private List<Data> parseResponse(ContentType accept, String response, List<Data> responseData) {

		try {

			if (response == null || response.trim().isEmpty()) {
				return responseData;
			}

			switch (accept.getMimeType()) {

			case "application/json":

				Gson gson = new GsonBuilder().create();
				JsonElement jsonElement = gson.fromJson(response, JsonElement.class);

				if (jsonElement.isJsonArray()) {
					jsonElement = jsonElement.getAsJsonArray().get(0);
				}

				for (Data data : responseData) {
					String[] dts = data.getName().split("\\.");
					if (dts.length == 1) {
						String type = data.getType();

						if (type.trim().equalsIgnoreCase("Duplicate")) {
							List<String> lstData = getListOfStringByName(response, data.getName());
							data.setValue(lstData);
						} else {
							JsonElement jsonElementData = getJsonElementByName(jsonElement, data.getName());// jsonElement.getAsJsonObject().get(data.getName());
							String value = jsonElementData != null && !jsonElementData.isJsonNull()
									? (jsonElementData.isJsonObject() ? jsonElementData.toString()
											: jsonElementData.getAsString())
									: null;
							data.setValue(value);
						}
					} else {
						JsonElement copy = jsonElement.deepCopy();
						for (int i = 0; i < dts.length; i++) {
							String dt = dts[i];
							if (dt != null && !dt.isEmpty() && dt.trim().contains("[") && dt.trim().contains("]")) {
								// JSON Array
								String name = dt.substring(0, dt.indexOf('['));
								String arrIndex = dt.substring((dt.indexOf('[') + 1), dt.indexOf(']'));
								int index = -1;
								try {
									index = Integer.parseInt(arrIndex);
								} catch (Exception ex) {
									index = -1;
								}

								String type = data.getType();

								if (type.trim().equalsIgnoreCase("Duplicate")) {
									List<String> lstData = getListOfStringByName(response, data.getName());
									data.setValue(lstData);
									copy = null;
									break;
								} else {

									JsonElement jsonElementData = getJsonElementByName(copy, name);// jsonElement.getAsJsonObject().get(data.getName());
									JsonArray jsonArrayElement = jsonElementData != null
											? jsonElementData.getAsJsonArray()
											: null;

									if (jsonArrayElement != null && index >= 0 && jsonArrayElement.size() > 0
											&& index < jsonArrayElement.size()) {
										copy = jsonArrayElement.get(index);
									} else {
										copy = jsonArrayElement;
									}
								}
							} else {
								// JSON Object
								String type = data.getType();
								if (type.trim().equalsIgnoreCase("Duplicate") && i == (dts.length - 1)) {
									List<String> lstData = getListOfStringByName(response, dt);
									data.setValue(lstData);
									copy = null;
									break;
								} else if (copy.isJsonArray()) {
									List<String> lstData = new ArrayList<String>();
									JsonArray jsonArr = copy.getAsJsonArray().deepCopy();
									for (int j = 0; j < jsonArr.size(); j++) {
										JsonObject jsonObj = jsonArr.get(j).getAsJsonObject();
										lstData.add(jsonObj.get(dt).getAsString());
									}
									data.setValue(lstData);
									copy = null;
								} else {
									copy = getJsonElementByName(copy, dt);
								}
							}
						}
						if (copy != null) {
							String value = null;
							if (copy.isJsonArray()) {
								List<String> values = new ArrayList<String>();
								for (JsonElement ele : copy.getAsJsonArray()) {
									value = ele != null && !ele.isJsonNull() ? ele.getAsString() : null;
									values.add(value);
								}
								data.setValue(values);
							} else {
								value = copy != null && !copy.isJsonNull() ? copy.getAsString() : null;
								data.setValue(value);
							}

						}
					}
				}

				break;

			case "application/xml":
			case "text/xml":

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);

				DocumentBuilder builder = dbf.newDocumentBuilder();

				Document doc = builder.parse(new ByteArrayInputStream(response.getBytes()));

				for (Data data : responseData) {
					String[] dts = data.getName().split("\\.");
					if (dts.length == 1) {
						NodeList nodeList = doc.getElementsByTagNameNS("*", data.getName());
						String value = nodeList != null && nodeList.item(0) != null ? nodeList.item(0).getTextContent()
								: null;
						data.setValue(value);
					} else {
						Document copy = builder.parse(new ByteArrayInputStream(response.getBytes()));
						Node node = copy.getFirstChild();
						for (String tagName : dts) {

							String[] splitedTag = tagName.split(":");
							String dt = splitedTag != null ? splitedTag.length == 1 ? splitedTag[0] : splitedTag[1]
									: null;

							if (dt != null && !dt.isEmpty() && dt.trim().contains("[") && dt.trim().contains("]")) {
								// XML Array
								String name = dt.substring(0, dt.indexOf('['));
								String arrIndex = dt.substring((dt.indexOf('[') + 1), dt.indexOf(']'));
								int index = -1;
								try {
									index = Integer.parseInt(arrIndex);
								} catch (Exception ex) {
									index = -1;
								}

								NodeList nodeList = copy != null ? copy.getElementsByTagNameNS("*", name) : null;

								if (nodeList != null && index >= 0 && nodeList.getLength() > 0
										&& index < nodeList.getLength()) {
									node = nodeList.item(index);
									copy = nodeList.item(index) != null ? nodeList.item(index).getOwnerDocument()
											: null;
								} else {
									for (int i = 0; nodeList != null && nodeList.getLength() > 0
											&& i < nodeList.getLength(); i++) {
										node = nodeList.item(i);
										String value = node.getTextContent();
										data.setValue(value);
										node = null;
									}
								}
							} else {
								// XML Object
								NodeList nodeList = copy != null ? copy.getElementsByTagNameNS("*", dt) : null;
								node = nodeList != null ? nodeList.item(0) : null;
								copy = nodeList != null && nodeList.item(0) != null
										? nodeList.item(0).getOwnerDocument()
										: null;
							}
						}
						if (node != null) {
							String value = node != null ? node.getTextContent() : null;
							data.setValue(value);
						}
					}
				}
				break;

			default:
				break;

			}

		} catch (Exception ex) {
			log.error("Web Service | Parse Response | Exception : " + ex.getMessage(), ex);
		}

		return responseData;
	}

	private JsonElement getJsonElementByName(JsonElement jsonElement, String name) {

		JsonElement foundJsonElement = null;

		try {
			foundJsonElement = jsonElement.getAsJsonObject().get(name);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return foundJsonElement;

	}

	private List<String> getListOfStringByName(String jsonString, String name) {
		List<String> arrString = new ArrayList<String>();
		com.fasterxml.jackson.core.JsonParser parser = null;
		try {
			com.fasterxml.jackson.core.JsonFactory factory = new com.fasterxml.jackson.core.JsonFactory();
			parser = factory.createJsonParser(jsonString);
			parser.nextToken();

			while (parser.nextToken() != com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE) {
				String fieldName = parser.getCurrentName();

				if (fieldName == null) {
					break;
				}
				if (fieldName.equals(name)) {
					parser.nextToken();
					arrString.add(parser.getText());
				}
			}

		} catch (Exception ex) {
			log.error("Web Service | Get List of String By Name | Exception : " + ex.getMessage(), ex);
		} finally {
			try {
				if (parser != null) {
					parser.close();
				}
			} catch (Exception ex) {
				log.error("Web Service | Get List of String By Name | Finally | Exception : " + ex.getMessage(), ex);
			}
		}

		return arrString;
	}
}
