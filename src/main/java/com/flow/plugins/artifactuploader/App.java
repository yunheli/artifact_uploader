/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flow.plugins.artifactuploader;

import com.flow.plugins.artifactuploader.domain.Artifact;
import com.flow.plugins.artifactuploader.domain.Storage;
import com.flow.plugins.artifactuploader.exception.PluginException;
import com.flow.plugins.artifactuploader.util.CommonUtil;
import com.flow.plugins.artifactuploader.util.HttpClient;
import com.flow.plugins.artifactuploader.util.HttpResponse;
import com.flow.plugins.artifactuploader.util.Strings;
import com.flow.plugins.artifactuploader.util.ZipUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;


/**
 * @author yh@firim
 */
public class App {

    private final static Logger LOGGER = LoggerFactory.build(App.class);

    private final static String STORAGE_ROUTES = "/storages";

    private final static String ARTIFACT_ROUTES = "/artifacts";

    private final static String FLOW_API_DOMAIN = "FLOW_API_DOMAIN";

    private final static String PLUGIN_ARTIFACT_PATH = "PLUGIN_ARTIFACT_PATH";

    private final static String JOB_ID = "FLOW_JOB_ID";

    private final static Gson GSON = new Gson();

    private static String api;

    private static BigInteger jobID;

    private static String artifactPath;

    public static void main(String[] args) {

        System.out.println(CommonUtil.showJfigletMessage("Artifact Upload Start"));

        initSettings();

        // if not found artifact path return
        if (Strings.isNullOrEmpty(artifactPath)) {
            return;
        }

        startUpload();

        System.out.println(CommonUtil.showJfigletMessage("Artifact Upload Finish"));
    }

    private static void startUpload() {
        LOGGER.info("Start upload");

        Path path = Paths.get(artifactPath);
        Path zipPath = Paths.get(path.getParent().toString(), path.getFileName() + ".zip");

        // if not directory upload
        if (!path.toFile().isDirectory()) {
            doUpload(path);
        } else {
            // zip folder to upload
            try {
                ZipUtil.zipFolder(path.toFile(), zipPath.toFile());
            } catch (IOException e) {
                LOGGER.warn("Not found FLOW_API_DOMAIN");
                LOGGER.warn("ZIP FILE ERROR");
                exit();
            }
            doUpload(zipPath);

        }
    }

    private static void doUpload(Path path) {
        Storage storage = doUploadToLocal(path);
        recordArtifact(storage);
    }

    private static Storage doUploadToLocal(Path path) {
        String url = api + STORAGE_ROUTES;

        HttpEntity entity = MultipartEntityBuilder.create()
            .addPart("file", new FileBody(path.toFile()))
            .setContentType(ContentType.MULTIPART_FORM_DATA)
            .build();
        HttpResponse<String> response = HttpClient.build(url)
            .post(entity)
            .retry(5)
            .bodyAsString();
        if (response.hasSuccess()) {
            Storage storage = GSON.fromJson(response.getBody(), Storage.class);
            LOGGER.info("Finish upload  download url : " + storage.getUrl());
            return storage;
        }

        LOGGER.warn("Upload to api exception " + response.getBody());
        throw new PluginException("Upload to api exception " + response.getBody());
    }

    private static void recordArtifact(Storage storage) {
        LOGGER.info("Start record artifact");
        try {
            Artifact artifact = new Artifact();
            artifact.setJobId(jobID);
            artifact.setName(storage.getName());
            artifact.setUrl(storage.getUrl());

            String artifactUrl = api + ARTIFACT_ROUTES;

            HttpResponse<String> artifactResponse = HttpClient.build(artifactUrl)
                .post(GSON.toJson(artifact))
                .withContentType(ContentType.APPLICATION_JSON)
                .retry(5)
                .bodyAsString();

            if (artifactResponse.hasSuccess()) {
                LOGGER.info("Finish Record artifact");
                return;
            }

            LOGGER.warn("Record artifact exception " + artifactResponse.getBody());

            throw new PluginException("Record artifact exception " + artifactResponse.getBody());
        } catch (UnsupportedEncodingException e) {
            throw new PluginException("Record artifact exception " + e.getMessage());
        }
    }

    private static void initSettings() {
        api = System.getenv(FLOW_API_DOMAIN);

        String strJobId = System.getenv(JOB_ID);

        artifactPath = System.getenv(PLUGIN_ARTIFACT_PATH);

        if (Strings.isNullOrEmpty(api)) {
            LOGGER.warn("Not found FLOW_API_DOMAIN");
            exit();
        }

        if (Strings.isNullOrEmpty(strJobId)) {
            LOGGER.warn("Not found FLOW_JOB_ID");
            exit();
        }

        jobID = new BigInteger(strJobId);


    }

    private static void exit() {
        System.exit(1);
    }
}
