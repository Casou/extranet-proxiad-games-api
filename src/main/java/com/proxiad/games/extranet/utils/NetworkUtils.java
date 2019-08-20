package com.proxiad.games.extranet.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtils {

    public static String getIpString(String port) {
        String ip;
        try {
            ip = NetworkUtils.getIpString();
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }

        if (ip.length() <= 16) {
            String fullIp = ip;
            fullIp += (port == null) ? "" : ":" + port;
            return fullIp;
        }

        return ip;
    }

    private static String getIpString() throws SocketException {
        StringBuilder ip = new StringBuilder();
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            Enumeration<InetAddress> i = e.nextElement().getInetAddresses();
            while (i.hasMoreElements()) {
                InetAddress a = i.nextElement();

                if (!a.isLoopbackAddress() && a.isSiteLocalAddress()) {
                    return a.getHostAddress();
                }

                ip.append(a.getHostName())
                        .append(" -> ")
                        .append(a.getHostAddress())
                        .append("\n\t isloopback? ")
                        .append(a.isLoopbackAddress())
                        .append("\n\t isSiteLocalAddress? ")
                        .append(a.isSiteLocalAddress())
                        .append("\n\t isIPV6? ")
                        .append((a instanceof Inet6Address))
                        .append("\n\n");
            }
        }
        return ip.toString();
    }

}
