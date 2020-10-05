package com.sn.ssh.sshql.directive;

/**
 * @author sonin
 * @date 2020/10/05
 */
public class ReplaceDirective extends InsertDirective {
    @Override
    public String getName() {
        return "replace";
    }

}
