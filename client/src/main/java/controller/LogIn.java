package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogIn {

    @FXML
    private Button buttonLogin;

    @FXML
    private TextField passworld;

    @FXML
    private TextField username;

    @FXML
    private Label wrongLogin;

    @FXML
    void userLogin(ActionEvent event) {
        // 1. Lấy dữ liệu người dùng nhập
        String user = username.getText();
        String pass = passworld.getText();

        // 2. Validate (Kiểm tra) cơ bản ngay tại Client để giảm tải cho Server
        if (user.trim().isEmpty() || pass.trim().isEmpty()) {
            wrongLogin.setText("Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            wrongLogin.setStyle("-fx-text-fill: red;"); // Đổi màu chữ thành đỏ
            return; // Dừng hàm lại ngay lập tức, không gửi lên Server nữa
        }

        // 3. Báo hiệu cho người dùng biết đang xử lý
        wrongLogin.setText("Đang kết nối tới máy chủ...");
        wrongLogin.setStyle("-fx-text-fill: blue;"); // Đổi sang màu xanh

        // 4. Đóng gói và gửi xuống tầng Mạng (Network)
        /*
         * LƯU Ý: Phần này là code gợi ý để bạn hình dung luồng đi của dữ liệu.
         * Tùy thuộc vào cách bạn viết class xử lý Network (Socket/REST),
         * bạn sẽ thay thế đoạn comment này bằng hàm gọi Network thực tế.
         */

        // Ví dụ: Tạo đối tượng DTO từ module auction-common
        // LoginRequestDTO requestDTO = new LoginRequestDTO(user, pass);

        // Truyền cho class Network gửi đi và đợi phản hồi...
        // NetworkManager.sendLoginRequest(requestDTO, response -> {
        //
        //     // QUAN TRỌNG: Mọi thay đổi giao diện từ kết quả của luồng mạng
        //     // ĐỀU PHẢI được bọc trong Platform.runLater
        //     Platform.runLater(() -> {
        //         if (response.isSuccess()) {
        //             wrongLogin.setText("Đăng nhập thành công! Đang vào phòng đấu giá...");
        //             wrongLogin.setStyle("-fx-text-fill: green;");
        //             // TODO: Viết hàm chuyển Scene sang giao diện màn hình chính ở đây
        //         } else {
        //             wrongLogin.setText("Sai tài khoản hoặc mật khẩu!");
        //             wrongLogin.setStyle("-fx-text-fill: red;");
        //         }
        //     });
        // });

        // Tạm thời in ra Console để bạn test xem nút bấm đã hoạt động chưa
        System.out.println("Đã thu thập: User = " + user + ", Pass = " + pass);
    }
}
