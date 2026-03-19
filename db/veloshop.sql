-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Mar 19, 2026 at 08:29 PM
-- Server version: 5.7.24
-- PHP Version: 8.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `veloshop`
--

-- --------------------------------------------------------

--
-- Table structure for table `brands`
--

CREATE TABLE `brands` (
  `brand_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `country` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `brands`
--

INSERT INTO `brands` (`brand_id`, `name`, `country`) VALUES
(1, 'Specialized', 'USA'),
(2, 'Shimano', 'Japan'),
(3, 'Cube', 'Germany'),
(4, 'SRAM', 'USA'),
(5, 'Trek', 'USA'),
(6, 'Giant', 'Taiwan'),
(7, 'Continental', 'Germany'),
(8, 'Schwalbe', 'Germany'),
(9, 'Canyon', 'Germany'),
(10, 'Scott', 'Switzerland');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `name`) VALUES
(1, 'Mountain velos'),
(2, 'Road velos'),
(3, 'Electric velos'),
(4, 'Brakes'),
(5, 'Tires'),
(6, 'Chains'),
(7, 'Pedals'),
(8, 'Helmets'),
(9, 'Lights'),
(10, 'Saddles');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_id`, `first_name`, `last_name`, `username`, `password`, `email`, `phone`, `address`, `created_at`) VALUES
(1, 'Иван', 'Иванов', 'ivan_iv', '123456', 'ivan@mail.bg', '0888123456', 'София, ул. Витоша 1', '2026-03-19 18:31:07'),
(2, 'Мария', 'Петрова', 'maria_p', '123456', 'maria@abv.bg', '0877223344', 'Пловдив, ул. Главна 5', '2026-03-19 18:31:07'),
(3, 'Георги', 'Димитров', 'joro_d', '123456', 'joro@gmail.com', '0899556677', 'Варна, бул. Приморски 10', '2026-03-19 18:31:07'),
(4, 'Елена', 'Стоянова', 'elena_s', '123456', 'elena@office.bg', '0885112233', 'Бургас, жк. Лазур', '2026-03-19 18:31:07'),
(5, 'Стефан', 'Колев', 'stefan_k', '123456', 'stefan@velo.com', '0876998877', 'Стара Загора, ул. Шипка 3', '2026-03-19 18:31:07'),
(6, 'Анелия', 'Цветанова', 'ani_tz', '123456', 'ani@mail.bg', '0887445566', 'Русе, ул. Дунав 12', '2026-03-19 18:31:07'),
(7, 'Николай', 'Ангелов', 'niki_a', '123456', 'niki@pro.bg', '0894112233', 'Плевен, ул. Васил Левски 2', '2026-03-19 18:31:07'),
(8, 'Петър', 'Василев', 'pesho_v', '123456', 'pesho@test.com', '0882334455', 'Велико Търново, ул. Гурко', '2026-03-19 18:31:07'),
(9, 'Десислава', 'Радева', 'desi_r', '123456', 'desi@sunny.bg', '0879667788', 'Благоевград, ул. Пирин', '2026-03-19 18:31:07'),
(10, 'Мартин', 'Тодоров', 'marto_t', '123456', 'marto@hub.bg', '0883009988', 'Хасково, бул. България', '2026-03-19 18:31:07');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `order_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('Pending','Paid','Shipped','Cancelled') DEFAULT 'Pending',
  `total_amount` decimal(10,2) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `customer_id`, `order_date`, `status`, `total_amount`) VALUES
(11, 1, '2026-03-19 22:06:27', 'Paid', '5630.00'),
(12, 1, '2026-03-19 22:10:32', 'Paid', '5400.00'),
(13, 1, '2026-03-19 22:10:35', 'Paid', '4200.00'),
(14, 1, '2026-03-19 22:10:39', 'Paid', '320.50'),
(15, 1, '2026-03-19 22:21:07', 'Paid', '6361.50'),
(16, 1, '2026-03-19 22:21:22', 'Paid', '10800.00'),
(17, 1, '2026-03-19 22:21:29', 'Paid', '641.00'),
(18, 1, '2026-03-19 22:27:12', 'Paid', '1380.00');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `price_at_sale` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`order_item_id`, `order_id`, `product_id`, `quantity`, `price_at_sale`) VALUES
(11, 11, 1, 1, '5400.00'),
(12, 11, 4, 2, '115.00'),
(13, 12, 1, 1, '5400.00'),
(14, 13, 3, 1, '4200.00'),
(15, 14, 2, 1, '320.50'),
(16, 15, 1, 1, '5400.00'),
(17, 15, 2, 1, '320.50'),
(18, 15, 2, 1, '320.50'),
(19, 15, 2, 1, '320.50'),
(20, 16, 1, 2, '5400.00'),
(21, 17, 2, 2, '320.50'),
(22, 18, 4, 6, '115.00'),
(23, 18, 4, 6, '115.00');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `brand_id` int(11) DEFAULT NULL,
  `supplier_id` int(11) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `specifications` text,
  `stock_quantity` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `name`, `category_id`, `brand_id`, `supplier_id`, `price`, `specifications`, `stock_quantity`) VALUES
(1, 'Stumpjumper Comp', 1, 1, 1, '5400.00', 'Carbon frame, 29 inch wheels, SRAM drivetrain', 0),
(2, 'Shimano XT Brakes', 4, 2, 2, '320.50', 'Hydraulic disc brakes, 4-piston', 9),
(3, 'Cube Reaction Hybrid', 3, 3, 5, '4200.00', 'Bosch Motor 85Nm, 625Wh Battery', 2),
(4, 'GP 5000 Tire', 5, 7, 3, '115.00', '700x25c, Foldable, BlackChili compound', 26),
(5, 'SRAM GX Eagle Chain', 6, 4, 4, '65.00', '12-speed, Silver, 126 links', 25),
(6, 'Trek Domane SL5', 2, 5, 1, '6100.00', 'Endurance road velo, Shimano 105', 2),
(7, 'Giant Trance X', 1, 6, 7, '4800.00', 'Full suspension, 150mm travel', 4),
(8, 'Schwalbe Magic Mary', 5, 8, 3, '95.00', '29x2.4, Super Gravity, Addix Soft', 20),
(9, 'Scott Spark RC', 1, 10, 10, '7500.00', 'Cross Country race velo, Integrated shock', 1),
(10, 'Shimano Saint Pedals', 7, 2, 6, '180.00', 'Flat pedals, DH focused, replaceable pins', 12);

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `contact_email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `name`, `contact_email`, `phone`) VALUES
(1, 'velo Distro Ltd', 'info@velodistro.com', '0888111222'),
(2, 'Euro Parts', 'sales@europarts.de', '+49123456'),
(3, 'Global Cycling', 'contact@globalcycling.com', '0877333444'),
(4, 'Fast Ship Gears', 'gears@fastship.com', '029887766'),
(5, 'Velo Supply', 'velo@supply.bg', '0899000111'),
(6, 'Component Pros', 'pros@comp.com', '0885123456'),
(7, 'Direct velo', 'direct@velo.com', '0876543210'),
(8, 'Trade Wheels', 'trade@wheels.com', '0887112233'),
(9, 'Eco Trade', 'eco@trade.com', '0895998877'),
(10, 'Cycle Wholesale', 'wholesale@cycle.com', '029554433');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `brands`
--
ALTER TABLE `brands`
  ADD PRIMARY KEY (`brand_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`),
  ADD UNIQUE KEY `idx_username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `category_id` (`category_id`),
  ADD KEY `brand_id` (`brand_id`),
  ADD KEY `supplier_id` (`supplier_id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `brands`
--
ALTER TABLE `brands`
  MODIFY `brand_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`),
  ADD CONSTRAINT `products_ibfk_2` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`brand_id`),
  ADD CONSTRAINT `products_ibfk_3` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
