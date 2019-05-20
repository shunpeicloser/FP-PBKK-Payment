package me.pyradian.ojackpayment.service;

public interface AccountingAPICallService {
    boolean createPengeluaran(String tanggal, int jumlah, String keterangan);
    boolean createPendapatan(String tanggal, int jumlah);
}
