package org.mavirgil.pixelsort.config;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ApiPath {

    //Base paths
    public static final String API_BASE = "/v1";
    public static final String IMAGE_BASE = API_BASE + "/image";

    //Path variables
    public static final String ID = "{id}";

}
