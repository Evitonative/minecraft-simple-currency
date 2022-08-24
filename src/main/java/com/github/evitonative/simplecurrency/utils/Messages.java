package com.github.evitonative.simplecurrency.utils;

@SuppressWarnings("unused")
public enum Messages {
    BALANCE("balance"),
    BALANCE_OTHER("balance-others"),
    PAYED("payed"),
    RECEIVED("payed"),
    ;

    public enum Bank{
        TITLE("title"),
        CURRENT_BALANCE("current-balance"),
        DEPOSIT("deposit"),
        WITHDRAW("withdraw"),
        PAY("pay"),
        ;

        static String path;
        Bank(String path){}

        public String getPath(){
            return "messages.bank." + path;
        }
    }

    public enum Withdrawal{
        TITLE("title"),
        ITEMS("items"),
        ;

        static String path;
        Withdrawal(String path){}

        public String getPath(){
            return "messages.withdrawal." + path;
        }
    }

    public enum Deposit{
        SUCCESS("success"),
        INVALID("invalid"),
        ;

        static String path;
        Deposit(String path){}

        public String getPath(){
            return "messages.deposit." + path;
        }
    }

    public enum Errors{
        CONSOLE("console"),
        NO_SELF("no-self"),
        NO_NEGATIVES("mno-negatives"),
        NOT_ENOUGH_MONEY("not-enough-money"),
        NOT_ENOUGH_SPACE("not-enough-space"),
        INVALID_PLAYER("invalid-player"),
        INVALID_PLAYER_OFFLINE("invalid-player-offline"),
        INT_REQUIRED("int-required"),
        MISSING_ARGUMENTS("missing-arguments"),
        MISSING_PERMISSIONS("missing-permissions"),
        ;

        static String path;
        Errors(String path){}

        public String getPath(){
            return "messages.errors." + path;
        }
    }

    static String path;
    Messages(String path){}

    public String getPath(){
        return "messages." + path;
    }
}
