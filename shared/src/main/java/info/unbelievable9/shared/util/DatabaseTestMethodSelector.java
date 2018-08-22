package info.unbelievable9.shared.util;

import org.testng.IMethodSelector;
import org.testng.IMethodSelectorContext;
import org.testng.ITestNGMethod;

import java.util.List;

/**
 * Created on : 2018/8/22
 * Author     : Unbelievable9
 **/
public class DatabaseTestMethodSelector implements IMethodSelector {

    private boolean containsString(String[] strings, String target) {
        for (String string : strings) {
            if (string.equals(target)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean includeMethod(IMethodSelectorContext iMethodSelectorContext, ITestNGMethod iTestNGMethod, boolean b) {
        String[] groups = iTestNGMethod.getGroups();

        if (groups == null || groups.length == 0) {
            return true;
        }

        String database = System.getProperty("database");

        if (database == null && containsString(groups, "H2")) {
            return true;
        }

        if (database != null && containsString(groups, database)) {
            return true;
        }

        iMethodSelectorContext.setStopped(true);

        return false;
    }

    @Override
    public void setTestMethods(List<ITestNGMethod> list) {
    }
}
