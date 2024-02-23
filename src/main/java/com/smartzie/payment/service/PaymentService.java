package com.smartzie.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartzie.payment.component.MultiPartFile;
import com.smartzie.payment.component.Response;
import com.smartzie.payment.component.ResponseCode;
import com.smartzie.payment.component.UUIDGenerator;
import com.smartzie.payment.model.Payment;
import com.smartzie.payment.model.VirtualAccount;
import com.smartzie.payment.repository.PaymentRepository;
import com.smartzie.payment.repository.VirtualAccountRepository;
import com.smartzie.payment.request.CallbackRequest;
import com.smartzie.payment.request.PaymentRequest;
import com.smartzie.payment.request.ReportRequest;
import com.smartzie.payment.request.TransferRequest;
import com.xendit.Xendit;
import com.xendit.exception.XenditException;
import com.xendit.model.Disbursement;
import com.xendit.model.FixedVirtualAccount;
import org.apache.coyote.Request;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private GoogleDriveService uploadService;
    @Autowired
    private VirtualAccountRepository virtualAccountRepository;

    public ResponseEntity<?> bayar(String keterangan, String accountNumber, String name, String bank){
        Response<?> response = new Response<>();
//        xnd_development_IusTKWJOC80Q4ZSkHsy5TehOdZuQMKNanPUOlKrJUP5yKhw8MEv953SRnZwf1PV
//        xnd_development_Kt2uihC79NlVHQoxvlDX4hT8gCEgFfBNETfcRSBjpKKJ7wyTp62X36pPnvtIj
        Xendit.Opt.setApiKey("xnd_development_Kt2uihC79NlVHQoxvlDX4hT8gCEgFfBNETfcRSBjpKKJ7wyTp62X36pPnvtIj");
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("external_id", "my_external_id");
            params.put("bank_code", bank);
            params.put("account_holder_name", name);
            params.put("account_number", accountNumber);
            params.put("description", keterangan);
            params.put("amount", "90000");

            Disbursement disbursement = Disbursement.create(params);
        } catch (XenditException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Response<FixedVirtualAccount>> createVA(PaymentRequest request){
        Response<FixedVirtualAccount> response = new Response<>();
        response.setResponseCode(ResponseCode.SUCCESS);
        VirtualAccount va = new VirtualAccount();
        Xendit.Opt.setApiKey("xnd_development_Kt2uihC79NlVHQoxvlDX4hT8gCEgFfBNETfcRSBjpKKJ7wyTp62X36pPnvtIj");
        Map<String, Object> closedVAMap = new HashMap<>();
        String id = UUIDGenerator.idGenerator();
        closedVAMap.put("external_id", id);
        closedVAMap.put("bank_code", request.getBank());
        closedVAMap.put("name", request.getName());
        LocalDate expired = LocalDate.now(ZoneId.of("GMT+7")).plusDays(1);
        closedVAMap.put("expiration_date", expired.format(DateTimeFormatter.ISO_DATE));
        closedVAMap.put("expected_amount", request.getAmount());

        va.setBankCode(request.getBank());
        va.setExpiredDate(expired);
        va.setAmount(request.getAmount());
        va.setUserId(request.getSiswaId());
        va.setCreatedAt(Instant.now());
        va.setUpdatedAt(Instant.now());
        try {
            FixedVirtualAccount virtualAccount = FixedVirtualAccount.createClosed(closedVAMap);
            va.setId(virtualAccount.getId());
            System.out.println(virtualAccount.getId());
            response.setData(virtualAccount);
            virtualAccountRepository.save(va);
        } catch (XenditException e) {
            e.printStackTrace();
            response.setResponseCode(ResponseCode.BAD_REQUEST);
            response.setMessage("Data yang anda masukan tidak valid");
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Response<FixedVirtualAccount>> updateVA(PaymentRequest request){
        Response<FixedVirtualAccount> response = new Response<>();
        response.setResponseCode(ResponseCode.SUCCESS);
        Xendit.Opt.setApiKey("xnd_development_Kt2uihC79NlVHQoxvlDX4hT8gCEgFfBNETfcRSBjpKKJ7wyTp62X36pPnvtIj");
        Map<String, Object> closedVAMap = new HashMap<>();
        LocalDate expired = LocalDate.now(ZoneId.of("GMT+7"));
        closedVAMap.put("expiration_date", expired.plusDays(1).format(DateTimeFormatter.ISO_DATE));
        closedVAMap.put("expected_amount", request.getAmount());
        FixedVirtualAccount fpa;
        try {
            fpa = FixedVirtualAccount.getFixedVA(request.getId());
        } catch (XenditException e) {
            throw new RuntimeException(e);
        }
        VirtualAccount va = virtualAccountRepository.findById(request.getId()).orElseThrow();
        va.setBankCode(request.getBank());
        va.setExpiredDate(expired);
        va.setAmount(request.getAmount());
        va.setUserId(request.getSiswaId());
        va.setUpdatedAt(Instant.now());
        try {
            FixedVirtualAccount virtualAccount = FixedVirtualAccount.update(request.getId(), closedVAMap);
            response.setData(virtualAccount);
            virtualAccountRepository.save(va);
        } catch (XenditException e) {
            e.printStackTrace();
            response.setResponseCode(ResponseCode.BAD_REQUEST);
            response.setMessage("Data yang anda masukan tidak valid");
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> viaTransfer(TransferRequest request){

        Response<?> response = new Response<>();
        response.setResponseCode(ResponseCode.SUCCESS);
        Payment data = new Payment();
        LocalDate now = LocalDate.now();
        List<String> status = List.of("Valid", "Process", "Progress");
        if (paymentRepository.existsBySiswaIdAndTanggalPembayaranBetweenAndStatusIn(request.getSiswaId(), now.withDayOfMonth(1), now.plusMonths(1).withDayOfMonth(1), status)){
            response.setResponseCode(ResponseCode.DUPLICATE_DATA);
            response.setMessage("Anda telah melakukan pembayaran pada bulan ini.");
            return ResponseEntity.status(409).body(response);
        }
        String id = UUIDGenerator.idGenerator();
        data.setId(id);

        data.setTanggalPembayaran(LocalDate.now());
        data.setJumlahBayar(request.getJumlahPembayaran());
        data.setTipePembayaran("via-transfer");
        data.setStatus("Process");
        data.setSiswaId(request.getSiswaId());
        data.setImage(image(request.getImage()));
        data.setCreatedAt(Instant.now());
        data.setUpdatedAt(Instant.now());
        paymentRepository.save(data);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> validasi(TransferRequest request){
        Response<?> response = new Response<>();
        response.setResponseCode(ResponseCode.SUCCESS);
        System.out.println(request.getStatus());
        if (!paymentRepository.existsById(request.getId())){
            response.setResponseCode(ResponseCode.BAD_REQUEST);
            return ResponseEntity.badRequest().body(response);
        }
        Payment data = paymentRepository.findById(request.getId()).orElseThrow();
        data.setStatus(request.getStatus());
        data.setUpdatedAt(Instant.now());
        paymentRepository.save(data);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> paidCallback(CallbackRequest request){
        Response<?> response = new Response<>();
        response.setResponseCode(ResponseCode.SUCCESS);
        VirtualAccount va = virtualAccountRepository.findById(request.getExternal_id()).orElseThrow();
        Payment data = paymentRepository.findByVirtualAccountAndStatus(va, "Progress");
        data.setStatus("Valid");
        paymentRepository.save(data);
        return ResponseEntity.ok(response);
    }

    private String image(String image){
        MultiPartFile file = new MultiPartFile(image);
        System.out.println(file.getName());
        return uploadService.uploadFile(file);
    }

    private String detectContentType(byte[] data) throws IOException {
        Tika tika = new Tika();
        return tika.detect(data);
    }

    public ResponseEntity<?> viaVa(PaymentRequest request){
        Response<String> response = new Response<>();
        response.setResponseCode(ResponseCode.SUCCESS);
        Payment data = new Payment();
        LocalDate now = LocalDate.now();
        if (paymentRepository.existsBySiswaIdAndTanggalPembayaranBetween(request.getSiswaId(), now.withDayOfMonth(1), now.plusMonths(1).withDayOfMonth(1))){
            response.setResponseCode(ResponseCode.DUPLICATE_DATA);
            response.setMessage("Anda telah melakukan pembayaran pada bulan ini.");
            return ResponseEntity.status(409).body(response);
        }
        String id = UUIDGenerator.idGenerator();
        data.setId(id);
        data.setTanggalPembayaran(LocalDate.now());
        data.setJumlahBayar(BigDecimal.valueOf(request.getAmount()));
        data.setTipePembayaran("via-virtual-account");
        data.setStatus("Progress");
        data.setSiswaId(request.getSiswaId());
        data.setCreatedAt(Instant.now());
        data.setUpdatedAt(Instant.now());
        FixedVirtualAccount node = null;
        if(virtualAccountRepository.existsByBankCodeAndUserId(request.getBank(), request.getSiswaId())){
            String siswa = virtualAccountRepository.findByUserIdAndBankCode(request.getSiswaId(), request.getBank()).getId();
            request.setId(siswa);
            node = Objects.requireNonNull(updateVA(request).getBody()).getData();
        }else{
            node = Objects.requireNonNull(createVA(request).getBody()).getData();
        }

        data.setVirtualAccount(virtualAccountRepository.findById(node.getId()).orElseThrow());
        paymentRepository.save(data);
        response.setData(node.getAccountNumber());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> process(){
        Response<List<Payment>> response = new Response<>();
        response.setResponseCode(ResponseCode.SUCCESS);
        response.setData(paymentRepository.findAllByStatus("Process"));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> valid(ReportRequest request){
        Response<List<Payment>> response = new Response<>();
        response.setResponseCode(ResponseCode.SUCCESS);
        if (request.getStartDate().isEmpty() && request.getEndDate().isEmpty()){
            response.setData(paymentRepository.findAllByStatus("Valid"));
        }else{
            LocalDate sd = LocalDate.parse(request.getStartDate(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate ed = LocalDate.parse(request.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            response.setData(paymentRepository.findAllByStatusAndTanggalPembayaranBetween("Valid", sd, ed));
        }
        return ResponseEntity.ok(response);
    }
}
