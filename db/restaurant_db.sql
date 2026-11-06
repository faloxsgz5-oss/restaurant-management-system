-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 17, 2025 at 09:42 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `restaurant_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `chef_orders`
--

CREATE TABLE `chef_orders` (
  `id` int(11) NOT NULL,
  `table_number` varchar(10) DEFAULT NULL,
  `menu_code` varchar(10) DEFAULT NULL,
  `menu_name` varchar(100) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `price` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chef_orders`
--

INSERT INTO `chef_orders` (`id`, `table_number`, `menu_code`, `menu_name`, `quantity`, `status`, `updated_at`, `price`, `remark`) VALUES
(1, 'A1', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:42', NULL, NULL),
(2, 'B1', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:15', NULL, NULL),
(3, 'C1', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:15', NULL, NULL),
(4, 'A2', NULL, NULL, NULL, '', '2025-04-25 17:43:15', NULL, NULL),
(5, 'B2', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:15', NULL, NULL),
(6, 'C2', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:15', NULL, NULL),
(7, 'A3', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:15', NULL, NULL),
(8, 'B3', NULL, NULL, NULL, '', '2025-04-25 17:43:15', NULL, NULL),
(9, 'C3', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:15', NULL, NULL),
(10, 'A4', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:15', NULL, NULL),
(11, 'B4', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:15', NULL, NULL),
(12, 'C4', NULL, NULL, NULL, 'ว่าง', '2025-04-25 17:43:15', NULL, NULL),
(14, 'A4', '007', 'โมจิถั่วแดงย่าง', 1, 'เสร็จแล้ว', '2025-04-25 17:43:15', 50, NULL),
(15, 'A3', '005', 'โซบะเย็น', 1, 'เสร็จแล้ว', '2025-04-25 18:01:34', 100, NULL),
(16, 'C2', '005', 'โซบะเย็น', 1, 'เสร็จแล้ว', '2025-04-30 05:30:45', 100, NULL),
(17, 'B4', '004', 'ราเมงโชยุ', 1, 'เสร็จแล้ว', '2025-04-30 05:58:41', 90, NULL),
(18, 'C1', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-04-30 06:44:49', 60, NULL),
(19, 'B1', '008', 'สาเกเย็น', 1, 'เสร็จแล้ว', '2025-04-30 07:25:20', 60, NULL),
(20, 'B3', '008', 'สาเกเย็น', 1, 'เสร็จแล้ว', '2025-04-30 10:23:39', NULL, NULL),
(21, 'C3', '008', 'สาเกเย็น', 1, 'เสร็จแล้ว', '2025-04-30 10:26:16', NULL, NULL),
(22, 'B2', '005', 'โซบะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-04-30 10:45:52', NULL, NULL),
(24, 'A2', '003', 'ซาบะย่างซีอิ๊ว', 1, 'เสร็จแล้ว', '2025-04-30 11:01:28', NULL, NULL),
(26, 'A1', '007', 'โมจิถั่วแดงย่าง', 1, 'อาหารเสร็จแล้ว', '2025-04-30 11:08:22', NULL, NULL),
(27, 'A1', '005', 'โซบะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-04-30 15:25:44', NULL, NULL),
(28, 'B1', '005', 'โซบะเย็น', 12, 'เสร็จแล้ว', '2025-05-01 09:18:46', NULL, NULL),
(30, 'A1', '005', 'โซบะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 14:16:35', NULL, NULL),
(31, 'A1', '002', 'แซลมอนย่างซีอิ๊ว', 1, 'อาหารเสร็จแล้ว', '2025-05-12 14:17:37', NULL, NULL),
(32, 'B2', '005', 'โซบะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 14:18:28', NULL, NULL),
(35, 'A1', '005', 'โซบะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 14:24:17', NULL, NULL),
(36, 'C1', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 14:25:07', NULL, NULL),
(37, 'A1', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 15:17:22', NULL, NULL),
(38, 'C1', '006', 'ยำสลัดแซลมอน', 1, 'อาหารเสร็จแล้ว', '2025-05-12 15:18:13', NULL, NULL),
(39, 'B2', '005', 'โซบะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 15:34:58', NULL, NULL),
(40, 'A2', '008', 'สาเกเย็น', 20, 'อาหารเสร็จแล้ว', '2025-05-12 15:44:52', NULL, 'เพิ่มน้ำแข็ง'),
(41, 'B2', '009', 'มันชะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 15:47:03', NULL, ''),
(42, 'A2', '008', 'สาเกเย็น', 2, 'อาหารเสร็จแล้ว', '2025-05-12 16:05:31', NULL, ''),
(43, 'A2', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:07:33', NULL, ''),
(45, 'B2', '004', 'ราเมงโชยุ', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:14:36', NULL, ''),
(46, 'B4', '009', 'มันชะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:28:38', NULL, 'หวานน้อย'),
(47, 'A3', '007', 'โมจิถั่วแดงย่าง', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:35:04', NULL, ''),
(48, 'A2', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:38:21', NULL, ''),
(49, 'B2', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:39:46', NULL, ''),
(50, 'C4', '005', 'โซบะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:42:35', NULL, ''),
(51, 'C4', '009', 'มันชะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:42:35', NULL, ''),
(52, 'C4', '006', 'ยำสลัดแซลมอน', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:42:35', NULL, ''),
(53, 'A2', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:45:09', NULL, ''),
(54, 'C3', '009', 'มันชะเย็น', 6, 'อาหารเสร็จแล้ว', '2025-05-12 16:55:28', NULL, 'หวานน้อย\nNo salad'),
(55, 'C3', '006', 'ยำสลัดแซลมอน', 5, 'อาหารเสร็จแล้ว', '2025-05-12 16:55:28', NULL, 'หวานน้อย\nNo salad'),
(57, 'A2', '007', 'โมจิถั่วแดงย่าง', 1, 'อาหารเสร็จแล้ว', '2025-05-12 16:58:56', NULL, 'หวานน้อย'),
(59, 'B2', '009', 'มันชะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 17:04:49', NULL, 'หวานน้อย\nน้ำแข็งน้อย'),
(60, 'A2', '006', 'ยำสลัดแซลมอน', 5, 'อาหารเสร็จแล้ว', '2025-05-12 17:09:27', NULL, ''),
(61, 'C1', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 17:09:33', NULL, ''),
(62, 'A4', '009', 'มันชะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-12 17:09:48', NULL, 'หวานน้อย'),
(63, 'A4', '006', 'ยำสลัดแซลมอน', 1, 'อาหารเสร็จแล้ว', '2025-05-12 17:09:48', NULL, 'หวานน้อย'),
(64, 'B2', '008', 'สาเกเย็น', 10, 'อาหารเสร็จแล้ว', '2025-05-13 13:44:47', NULL, ''),
(65, 'A1', '001', 'แซลมอนย่างเกลือ', 5, 'อาหารเสร็จแล้ว', '2025-05-17 10:06:12', NULL, ''),
(66, 'A1', '001', 'แซลมอนย่างเกลือ', 1, 'อาหารเสร็จแล้ว', '2025-05-17 10:07:44', NULL, ''),
(67, 'A1', '002', 'แซลมอนย่างซีอิ๊ว', 1, 'อาหารเสร็จแล้ว', '2025-05-17 10:07:44', NULL, ''),
(68, 'A1', '003', 'ซาบะย่างซีอิ๊ว', 1, 'อาหารเสร็จแล้ว', '2025-05-17 10:07:44', NULL, ''),
(69, 'A1', '004', 'ราเมงโชยุ', 1, 'อาหารเสร็จแล้ว', '2025-05-17 10:07:44', NULL, ''),
(70, 'A1', '005', 'โซบะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-17 10:07:44', NULL, ''),
(71, 'A1', '006', 'ยำสลัดแซลมอน', 1, 'อาหารเสร็จแล้ว', '2025-05-17 10:07:44', NULL, ''),
(72, 'A1', '007', 'โมจิถั่วแดงย่าง', 1, 'อาหารเสร็จแล้ว', '2025-05-17 10:07:44', NULL, ''),
(73, 'A1', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-17 10:07:44', NULL, ''),
(74, 'A1', '009', 'มันชะเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-17 10:07:44', NULL, ''),
(75, 'A1', '001', 'แซลมอนย่างเกลือ', 1, 'อาหารเสร็จแล้ว', '2025-05-17 19:00:51', NULL, ''),
(76, 'A1', '008', 'สาเกเย็น', 1, 'อาหารเสร็จแล้ว', '2025-05-17 19:00:51', NULL, ''),
(77, 'A1', '011', 'ซาชิมิ ฮามาจิ', 1, 'อาหารเสร็จแล้ว', '2025-05-17 19:00:51', NULL, ''),
(78, 'A1', '013', 'ทงคตสึราเม็ง', 1, 'อาหารเสร็จแล้ว', '2025-05-17 19:00:51', NULL, ''),
(79, 'A1', '001', 'แซลมอนย่างเกลือ', 1, 'ออเดอร์', '2025-05-17 19:03:30', NULL, ''),
(80, 'A1', '002', 'แซลมอนย่างซีอิ๊ว', 1, 'ออเดอร์', '2025-05-17 19:03:30', NULL, ''),
(81, 'A1', '005', 'โซบะเย็น', 1, 'ออเดอร์', '2025-05-17 19:03:30', NULL, ''),
(82, 'A1', '011', 'ซาชิมิ ฮามาจิ', 1, 'ออเดอร์', '2025-05-17 19:03:30', NULL, '');

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE `menu` (
  `id` int(11) NOT NULL,
  `code` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`id`, `code`, `name`, `price`) VALUES
(1, '001', 'แซลมอนย่างเกลือ', 120.00),
(2, '002', 'แซลมอนย่างซีอิ๊ว', 125.00),
(3, '003', 'ซาบะย่างซีอิ๊ว', 110.00),
(4, '004', 'ราเมงโชยุ', 90.00),
(5, '005', 'โซบะเย็น', 100.00),
(6, '006', 'ยำสลัดแซลมอน', 100.00),
(7, '007', 'โมจิถั่วแดงย่าง', 50.00),
(8, '008', 'สาเกเย็น', 60.00),
(9, '009', 'มันชะเย็น', 45.00),
(14, '010', 'ซูชิ โอโทโระ', 120.00),
(15, '011', 'ซาชิมิ ฮามาจิ', 110.00),
(16, '012', 'ซูชิ เอนกาวะ', 110.00),
(17, '012', 'อูนิ', 160.00),
(18, '013', 'ทงคตสึราเม็ง', 160.00),
(19, '014', 'ชิโอะราเม็ง', 160.00),
(20, '015', 'ซัปโปโรมิโสะราเม็ง', 160.00),
(21, '016', 'คุโระราเม็ง', 160.00),
(22, '017', 'เอบิเทมปุระ', 210.00),
(23, '018', 'คาราอะเกะ', 150.00),
(24, '019', 'กิวด้ง', 150.00),
(25, '020', 'อูนางิด้ง', 170.00),
(26, '021', 'ไดฟุกุ', 60.00),
(27, '022', 'ดันโกะ', 60.00),
(28, '023', 'คุโระมิทสึคินาโกะ', 80.00);

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` int(11) NOT NULL,
  `table_number` varchar(10) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `payment_time` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sales_data`
--

CREATE TABLE `sales_data` (
  `id` int(11) NOT NULL,
  `report_date` date DEFAULT NULL,
  `total_sales` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tables`
--

CREATE TABLE `tables` (
  `id` int(11) NOT NULL,
  `table_number` varchar(10) NOT NULL,
  `status` enum('โต๊ะว่าง','โต๊ะไม่ว่าง','ชำระเงินแล้ว') DEFAULT 'โต๊ะว่าง',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tables`
--

INSERT INTO `tables` (`id`, `table_number`, `status`, `updated_at`) VALUES
(1, 'A1', 'โต๊ะไม่ว่าง', '2025-05-17 19:03:30'),
(2, 'A2', 'ชำระเงินแล้ว', '2025-05-12 17:12:09'),
(3, 'A3', 'โต๊ะว่าง', '2025-05-12 16:36:07'),
(4, 'A4', 'โต๊ะว่าง', '2025-05-12 17:10:57'),
(5, 'B1', 'โต๊ะว่าง', '2025-05-12 15:44:01'),
(6, 'B2', 'โต๊ะว่าง', '2025-05-13 13:45:51'),
(7, 'B3', 'โต๊ะว่าง', '2025-05-12 15:44:01'),
(8, 'B4', 'โต๊ะว่าง', '2025-05-12 16:29:42'),
(9, 'C1', 'ชำระเงินแล้ว', '2025-05-12 17:12:11'),
(10, 'C2', 'โต๊ะว่าง', '2025-04-30 10:21:25'),
(11, 'C3', 'โต๊ะว่าง', '2025-05-12 16:59:28'),
(12, 'C4', 'โต๊ะว่าง', '2025-05-12 16:43:39');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(100) DEFAULT NULL,
  `password` varchar(50) NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `firstname`, `lastname`, `password`, `role`) VALUES
(1, '001', 'สมชาย', 'มงคลดี', 'pass001', 'manager'),
(2, '002', 'สุชาติ', 'พูนสุข', 'pass002', 'manager'),
(3, '003', 'จิตรลดา', 'ใจกล้า', 'pass003', 'manager'),
(4, '004', 'ธีรพล', 'เศรษฐี', 'pass004', 'manager'),
(5, '005', 'มณีรัตน์', 'ทองมาก', 'pass005', 'manager'),
(6, '006', 'วีรยุทธ', 'สุขสันต์', 'pass006', 'manager'),
(7, '007', 'พิทยา', 'แสงเงิน', 'pass007', 'manager'),
(8, '008', 'วิภา', 'วงศ์เจริญ', 'pass008', 'manager'),
(9, '009', 'กรรณิกา', 'เขียวสด', 'pass009', 'manager'),
(10, '010', 'สมบูรณ์', 'ดวงแก้ว', 'pass010', 'manager'),
(11, '011', 'สมหญิง', 'บุญมี', 'emp011', 'employee'),
(12, '012', 'ณัฐพล', 'เพ็ญศรี', 'emp012', 'employee'),
(13, '013', 'ธนพร', 'ศิริสุข', 'emp013', 'employee'),
(14, '014', 'อดิศักดิ์', 'รุ่งเรือง', 'emp014', 'employee'),
(15, '015', 'สุรีย์พร', 'อินทราชัย', 'emp015', 'employee'),
(16, '016', 'พงศธร', 'หอมหวล', 'emp016', 'employee'),
(17, '017', 'มยุรี', 'ชื่นบาน', 'emp017', 'employee'),
(18, '018', 'พชร', 'ไกรทอง', 'emp018', 'employee'),
(19, '019', 'นันทิยา', 'ธรรมชาติ', 'emp019', 'employee'),
(20, '020', 'เจษฎา', 'ทวีทรัพย์', 'emp020', 'employee'),
(21, '021', 'ยศพล', 'แซ่ลิ้ม', 'chef021', 'chef'),
(22, '022', 'วิษณุ', 'ยอดเยี่ยม', 'chef022', 'chef'),
(23, '023', 'ธันวา', 'ยอดเงิน', 'chef023', 'chef'),
(24, '024', 'ภูมินทร์', 'วัฒนาพันธ์', 'chef024', 'chef'),
(25, '025', 'กฤษณ์', 'ไทยแท้', 'chef025', 'chef'),
(26, '026', 'ชาญชัย', 'กล้าหาญ', 'chef026', 'chef'),
(27, '027', 'วราพล', 'เก่งกิจ', 'chef027', 'chef'),
(28, '028', 'นรีรัตน์', 'จิตรอารี', 'chef028', 'chef'),
(29, '029', 'ฐาปกรณ์', 'รักสงบ', 'chef029', 'chef'),
(30, '030', 'ณรงค์ชัย', 'ท่าทอง', 'Narongchai12', 'chef');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chef_orders`
--
ALTER TABLE `chef_orders`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sales_data`
--
ALTER TABLE `sales_data`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tables`
--
ALTER TABLE `tables`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chef_orders`
--
ALTER TABLE `chef_orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=83;

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sales_data`
--
ALTER TABLE `sales_data`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tables`
--
ALTER TABLE `tables`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
