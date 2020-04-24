package com.github.incognitojam.redpacket.engine.graphics;

import com.github.incognitojam.redpacket.engine.graphics.array.VertexArray;
import com.github.incognitojam.redpacket.engine.graphics.buffer.FloatArrayBuffer;
import com.github.incognitojam.redpacket.engine.graphics.buffer.IntArrayBuffer;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Mesh {
    private final VertexArray vao;
    private final FloatArrayBuffer positionsVbo;
    private final FloatArrayBuffer coloursVbo;
    private final IntArrayBuffer indicesVbo;
    private final int vertexCount;

    public Mesh(float[] positions, float[] colours, int[] indices) {
        vertexCount = indices.length;

        FloatBuffer positionsBuffer = null;
        FloatBuffer coloursBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vao = new VertexArray();
            vao.bind();

            positionsVbo = new FloatArrayBuffer(GL_ARRAY_BUFFER, GL_STATIC_DRAW);
            positionsBuffer = MemoryUtil.memAllocFloat(positions.length);
            positionsBuffer.put(positions).flip();
            positionsVbo.bind();
            positionsVbo.upload(positionsBuffer);
            vao.attrib(0, 3, GL_FLOAT, false, 0, 0);
            positionsVbo.unbind();

            coloursVbo = new FloatArrayBuffer(GL_ARRAY_BUFFER, GL_STATIC_DRAW);
            coloursBuffer = MemoryUtil.memAllocFloat(colours.length);
            coloursBuffer.put(colours).flip();
            coloursVbo.bind();
            coloursVbo.upload(coloursBuffer);
            vao.attrib(1, 3, GL_FLOAT, false, 0, 0);
            coloursVbo.unbind();

            indicesVbo = new IntArrayBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            indicesVbo.bind();
            indicesVbo.upload(indicesBuffer);
            indicesVbo.unbind();

            vao.unbind();
        } finally {
            if (positionsBuffer != null) {
                MemoryUtil.memFree(positionsBuffer);
            }
            if (coloursBuffer != null) {
                MemoryUtil.memFree(coloursBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void bind() {
        vao.bind();
        positionsVbo.bind();
        coloursVbo.bind();
        indicesVbo.bind();
        vao.enable();
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
    }

    public void unbind() {
        vao.disable();
        positionsVbo.unbind();
        coloursVbo.unbind();
        indicesVbo.unbind();
        vao.unbind();
    }

    public void destroy() {
        vao.disable();
        positionsVbo.destroy();
        coloursVbo.destroy();
        indicesVbo.destroy();
        vao.destroy();
    }
}
