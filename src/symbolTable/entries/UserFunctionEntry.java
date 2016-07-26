package symbolTable.entries;

import java.util.ArrayList;

public class UserFunctionEntry extends AFunctionEntry {

    /**
     * UserFunctionEntry Constructor
     *
     * @param name
     * @param arguments
     * @param scope
     */
    public UserFunctionEntry(String name,
            ArrayList<FormalVariableEntry> arguments, int scope) {
        super(name, arguments, scope);
    }

    @Override
    public String toString() {
        String string = super.toString();
        string += "\n\t\tArguments";
        for (FormalVariableEntry formal : this._arguments) {
            string += "\n\t\t" + formal.toString().replaceAll("\t", "\t\t\t");
        }
        string += "\n";

        return string;
    }
}
