package com.example.icontacts;

import java.util.Comparator;

public class ContactGroup {
    private String GroupName,GroupMembers;
    private int sno;

    public ContactGroup() {

    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupMembers() {
        return GroupMembers;
    }

    public ContactGroup(String groupName, String groupMembers) {
        GroupName = groupName;
        GroupMembers = groupMembers;
    }

    public void setGroupMembers(String groupMembers) {
        GroupMembers = groupMembers;
    }

    @Override
    public String toString() {
        return "ContactGroup{" +
                "GroupName='" + GroupName + '\'' +
                ", GroupMembers='" + GroupMembers + '\'' +
                ", sno=" + sno +
                '}';
    }

    public ContactGroup(String groupName, String groupMembers, int sno) {
        GroupName = groupName;
        GroupMembers = groupMembers;
        this.sno = sno;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public static Comparator<ContactGroup> contactsComparator = new Comparator<ContactGroup>() {


        public int compare(ContactGroup c1, ContactGroup c2) {
            String name1 = c1.getGroupName().toUpperCase();
            String name2 = c2.getGroupName().toUpperCase();


            return name1.compareTo(name2);

        }
    };

}
