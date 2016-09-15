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
package utils;

import symbols.value.StaticVal;
import symbols.value.Value;
import symbols.value.Value_t;

/**
 * CLASS Constants
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class Constants {

    public static final int GLOBAL_ENV_SCOPE = 0;
    public static final int FUNCTION_ENV_INIT_SCOPE = 0;

    public static final String LIBRARY_FUNC_ARG = "#libraryFunctionArg_";

    public static final String BREAK = "BREAK";
    public static final String CONTINUE = "CONTINUE";
    public static final String RETURN = "RETURN";

    public static final Value NULL = new StaticVal(Value_t.NULL, Value_t.NULL.toString());
    public static final Value UNDEFINED = new StaticVal(Value_t.UNDEFINED, Value_t.UNDEFINED.toString());
}
