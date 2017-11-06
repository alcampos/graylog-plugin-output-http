package com.alexjck.http.output.senders;

import java.io.IOException;
import java.util.List;

import org.graylog2.plugin.Message;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpSender implements Sender {

	private static final Logger LOG = LoggerFactory.getLogger(HttpSender.class);

	private final String url;
	private final List<String> fieldList;
	private final Boolean shouldSendStream;

	boolean initialized = false;

	private OkHttpClient client;
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public HttpSender(final String url, final List<String> fieldList, final Boolean shouldSendStream) {
		this.url = url;
		this.fieldList = fieldList;
		this.shouldSendStream = shouldSendStream;
	}

	@Override
	public void initialize() {
		client = new OkHttpClient();
		initialized = true;
	}

	@Override
	public void stop() {
	}

	@Override
	public void send(Message message) {
		final JSONObject jsonObject = new JSONObject();
		if (shouldSendStream) {			
			jsonObject.put("stream", message.getStreams());
		}
		final JSONObject jsonMessage =  new JSONObject(); 
		for(String field: fieldList) {
			final Object value = message.getField(field);
			if (value != null) {
				jsonObject.put(field, value);
			}
		}
		jsonObject.put("json_event", jsonMessage.toString());
		final RequestBody body = RequestBody.create(JSON, jsonObject.toString());
		final Request request = new Request.Builder().url(url).post(body).build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				LOG.debug("Call failed, reason: " + e.getMessage());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				LOG.debug("Call success, code: " + response.code());
			}
		});
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
}
