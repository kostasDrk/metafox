/**
 * Metafox - A DYNAMIC, INTERPRETED, META-PROGRAMMING LANGUAGE, RUN AND
 * SUPPORTED BY ITS OWN INDEPENDENT INTERPRETER.
 *
 * UNIVERSITY OF CRETE (UOC)
 *
 * COMPUTER SCIENCE DEPARTMENT (UOC)
 *
 * https://www.csd.uoc.gr/
 *
 * CS-540 ADVANCED TOPICS IN PROGRAMMING LANGUAGES DEVELOPMENT
 *
 * LICENCE: This file is part of Metafox. Metafox is free: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 *
 * Metafox is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Metafox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016
 *
 */
package environment;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import libraryFunctions.LibraryFunction_t;
import libraryFunctions.LibraryFunctions;

import symbols.utils.Symbol;
import symbols.value.Value_t;
import symbols.value.DynamicVal;

import static utils.Constants.GLOBAL_ENV_SCOPE;

/**
 * CLASS GlobalEnv
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class GlobalEnv extends Environment {

    public GlobalEnv() {
        super(GLOBAL_ENV_SCOPE);

        //Insert the library functions in global environment.
        for (LibraryFunction_t libraryFunction : LibraryFunction_t.values()) {

            try {
                String name = libraryFunction.toString();
                // System.out.println(name);
                Method method = LibraryFunctions.class.getMethod(name, Environment.class);
                DynamicVal<String> varInfo = new DynamicVal(Value_t.LIBRARY_FUNCTION, method, libraryFunction.toString());
                Symbol symbol = new Symbol(name);
                super.insert(symbol, varInfo);

            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(GlobalEnv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
