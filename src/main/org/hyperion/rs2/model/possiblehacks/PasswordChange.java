package org.hyperion.rs2.model.possiblehacks;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/10/14
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordChange extends PossibleHack {

    public final String oldPassword;
    public final String newPassword;

    public PasswordChange(final String name, final String ip, final String date, final String oldPassword, final String newPassword) {
        super(name, ip, date);
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String toString() {
        return String.format("[@red@PASS@bla@]:  '%s' @blu@>@bla@ '%s' on %s.", oldPassword, newPassword, ip);
    }

}
