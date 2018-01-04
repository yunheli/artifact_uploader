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

package com.flow.plugins.artifactuploader.domain;

import com.google.gson.annotations.Expose;
import java.math.BigInteger;

/**
 * @author yh@firim
 */
public class Artifact {

    private Integer id;

    /**
     * flow name
     */
    private BigInteger jobId;

    /**
     * file name
     */
    private String name;

    /**
     * url
     */
    private String url;

    public Artifact() {
    }

    public Artifact(BigInteger jobId, String name, String url) {
        this.jobId = jobId;
        this.name = name;
        this.url = url;
    }

    public BigInteger getJobId() {
        return jobId;
    }

    public void setJobId(BigInteger jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Artifact artifact = (Artifact) o;

        return id != null ? id.equals(artifact.id) : artifact.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Artifact{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", url='" + url + '\'' +
            '}';
    }
}
