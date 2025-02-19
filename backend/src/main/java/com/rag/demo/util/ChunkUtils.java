package com.rag.demo.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ChunkUtils {

    public String chunkToStr(float[] chunks) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < chunks.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(chunks[i]);
        }
        sb.append("]");
        return sb.toString();
    }

}
