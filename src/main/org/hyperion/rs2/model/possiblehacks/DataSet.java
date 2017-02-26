package org.hyperion.rs2.model.possiblehacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 6/14/2016.
 */
public class DataSet {

    private final List<String> PASSWORDS, PROTOCOLS, ADDRESSES;

    private DataSet(List<String> passwords, List<String> protocols, List<String> addresses) {
        this.PASSWORDS = passwords;
        this.PROTOCOLS = protocols;
        this.ADDRESSES = addresses;
    }

    public DataSet() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public List<String> getPasswords() {
        return PASSWORDS;
    }

    public List<String> getProtocols() {
        return PROTOCOLS;
    }

    public List<String> getAddresses() {
        return ADDRESSES;
    }

}
