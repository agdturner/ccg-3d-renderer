/*
 * Copyright 2022 Centre for Computational Geography.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.r3d.io;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.v3d.core.V3D_Environment;
import uk.ac.leeds.ccg.v3d.geometry.V3D_Triangle;
import uk.ac.leeds.ccg.v3d.geometry.V3D_Vector;

/**
 * For reading STL Files.
 *
 * @author Andy Turner
 */
public class STL_Reader {

    public static void main(String[] args) {
        Path p = Paths.get("C:", "Users", "agdtu", "src", "agdt", "java",
                "generic", "ccg-render3d", "data", "Utah_teapot_(solid).stl");
        try {
            ArrayList<V3D_Triangle> ts = readBinary(p);
            System.out.println(ts.get(0));
        } catch (IOException ex) {
            Logger.getLogger(STL_Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Read the
     * <a href="https://en.wikipedia.org/wiki/STL_(file_format)#Binary_STL">binary
     * STL file</a> at the given Path. Everything is assumed to be little endian.
     *
     * @param p The file to read.
     * @return An ArrayList of triangles read from the file.
     */
    public static ArrayList<V3D_Triangle> readBinary(Path p) throws IOException {
        V3D_Environment e = new V3D_Environment();
        ArrayList<V3D_Triangle> r = new ArrayList<>();
        DataInputStream dis = new DataInputStream(new FileInputStream(p.toFile()));
        // Skip 80 bytes
        dis.read(new byte[80]);
        int nTriangles = Integer.reverseBytes(dis.readInt());
        System.out.println("Reading " + nTriangles + " triangles.");
        // read triangles
        while (dis.available() > 0) {
            // The normal to the triangle is not currently used.
            V3D_Vector n = new V3D_Vector(
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())),
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())),
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())));
            V3D_Vector pv = new V3D_Vector(
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())),
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())),
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())));
            V3D_Vector qv = new V3D_Vector(
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())),
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())),
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())));
            V3D_Vector rv = new V3D_Vector(
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())),
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())),
                    Float.intBitsToFloat(Integer.reverseBytes(dis.readInt())));
            // The attribute is not currently used.
            short attribute = Short.reverseBytes(dis.readShort());
            r.add(new V3D_Triangle(e, pv, qv, rv));
        }
        return r;
    }

}
