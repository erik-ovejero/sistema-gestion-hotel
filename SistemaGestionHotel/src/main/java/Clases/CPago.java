package Clases;

import java.sql.Date;

public class CPago {

    private int idPago;
    private int idReserva;
    private Date fechaPago;
    private double monto;
    private String medioPago;
    private String estadoPago;

    public CPago() {
    }

    public CPago(int idPago, int idReserva, Date fechaPago, double monto, String medioPago, String estadoPago) {
        this.idPago = idPago;
        this.idReserva = idReserva;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.medioPago = medioPago;
        this.estadoPago = estadoPago;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
}