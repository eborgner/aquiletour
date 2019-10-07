// Copyright (C) (2019) (Mathieu Bergeron) (mathieu.bergeron@cmontmorency.qc.ca)
//
// This file is part of aquiletour
//
// aquiletour is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// aquiletour is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with aquiletour.  If not, see <https://www.gnu.org/licenses/>


package ca.aquiletour.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class Json {

	private static Gson gson = new GsonBuilder().create();
	

	public static <T> T fromJson(Gson gson, String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}

	private static <T> T fromJson(Gson gson, Reader jsonReader, Class<T> classOfT) {
		return gson.fromJson(jsonReader, classOfT);
	}

	private static <T> T fromJson(Gson gson, Reader jsonReader, TypeToken<T> typeToken) {
		return gson.fromJson(jsonReader, typeToken.getType());
	}

	private static <T> T fromJson(Gson gson, InputStream jsonStream, Class<T> classOfT) {
		Reader jsonReader = new InputStreamReader(jsonStream);
		return fromJson(gson, jsonReader, classOfT);
	}

	private static <T> T fromJson(Gson gson, InputStream jsonStream, TypeToken<T> type) {
		Reader jsonReader = new InputStreamReader(jsonStream);
		return fromJson(gson, jsonReader, type);
	}

	public static <T> T fromJson(Gson gson, Path jsonPath, Class<T> classOfT) throws FileNotFoundException {
		InputStream jsonStream = FileVsJar.getInputStream(jsonPath);
		return fromJson(gson, jsonStream, classOfT);
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT) {
		return fromJson(gson, json, classOfT);
	}

	private static <T> T fromJson(InputStream jsonStream, Class<T> classOfT) {
		return fromJson(gson, jsonStream, classOfT);
	}

	private static <T> T fromJson(InputStream jsonStream, TypeToken<T> typeToken) {
		return fromJson(gson, jsonStream, typeToken);
	}

	public static <T> T fromJson(Path jsonPath, Class<T> classOfT) throws FileNotFoundException {
		InputStream jsonStream = FileVsJar.getInputStream(jsonPath);
		return fromJson(jsonStream, classOfT);
	}

	public static <T> T fromJson(Path jsonPath, TypeToken<T> typeToken) throws FileNotFoundException {
		InputStream jsonStream = FileVsJar.getInputStream(jsonPath);
		return fromJson(jsonStream, typeToken);
	}

	public static <T> T fromJson(File file, Class<T> classOfT) throws FileNotFoundException {
		InputStream stream = new FileInputStream(file);
		return fromJson(stream, classOfT);
	}

	public static String toJson(Object jsonObject) {
		return gson.toJson(jsonObject);
	}

	public static void writeJson(Object jsonObject, Path jsonPath) throws IOException {
		File jsonFile = jsonPath.toFile();
		String json = gson.toJson(jsonObject);
		writeJson(json, jsonFile);
	}

	public static void writeJson(String json, File jsonFile) throws IOException {
		FileWriter writer = new FileWriter(jsonFile);
		writer.write(json);
		writer.close();
	}


}
