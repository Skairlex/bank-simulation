package com.ntt.transaction.common;

public class TransactionsConstants {

    //ACCOUNTS CONSTANTS
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Cuenta no encontrada";
    public static final String EXIST_ACCOUNT_MESSAGE ="El número de cuenta ya existe";
    public static final String SUCCESSFUL_ACCOUNT_DELETE = "Cuenta eliminada exitosamente";
    public static final String ACCOUNT_DELETED_MESSAGE = "Account with id number {} was deleted";

    //TRANSACTION CONSTANTS
    public static final String TRANSACTION_NOT_FOUND_MESSAGE = "Transacción no encontrada";
    public static final String INSUFFICIENT_BALANCE_MESSAGE ="Saldo insuficiente";
    public static final String DIFFERENT_TYPE_ACCOUNT ="El número de cuenta no corresponde al tipo de cuenta especificado.";
    public static final String SUCCESSFUL_TRANSACTION_DELETE = "Transaction eliminada exitosamente";
    public static final String TRANSACTION_DELETED_MESSAGE = "Transaction with id number {} was deleted";
    public static final String TRANSACTION_TYPE_NOT_ALLOWED = "Tipo de movimiento no permitido , solo se acepta DEPOSITO o RETIRO";
    public static final String OPTION_WITHDRAWAL="RETIRO";
    public static final String OPTION_DEPOSIT="DEPOSITO";
    public static final String TRANSACTION_UPDATE_ERROR_MESSAGE = "Solo se puede editar la ultima transacción hecha";
    public static final String TRANSACTION_DELETE_ERROR_MESSAGE = "Solo se puede eliminar la ultima transacción hecha";

}
