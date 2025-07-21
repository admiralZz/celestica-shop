// Тип ресурса
export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    stockQuantity: number;
    categoryName: string;
    createdAt: Date;
    updatedAt: Date;
    imageId: number;
    imageUrl?: string;
}

// Тип ресурса
export interface UpdateProduct {
    id: number;
    name: string;
    description: string;
    price: number;
    stockQuantity: number;
    categoryName: string;
    image?: File
}

// Тип для запроса проверки заказа
export interface ChosenOrderItem {
    productId: number;
    quantity: number;
}

export interface GetOrdersResponse {
    orders: OrderBody[];
}

export interface CheckOrderResponse {
    items: OrderItem[];
    total: number;
}

export interface OrderItem {
    product: Product;
    quantity: number;
    price: number;
}

export interface CreateOrder {
    items: ChosenOrderItem[];
    email: string;
    phone: string;
    address: string;
}

export interface OrderBody {
    items: OrderItem[];
    email: string;
    phone: string;
    address: string;
    datetime: Date;
    total: number;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    email: string;
    ok: boolean;
}
export interface RegisterRequest {
    email: string;
    password: string;
}

export interface AuthResponse {
    id: number;
    email: string;
}

// Настройка почтового клиента
export interface MailSettings {
    host: string;
    port: string;
    username: string;
    hasPassword: boolean;
    protocol: string;
    auth: boolean;
    sslEnable: boolean;
}

// Настройка почтового клиента
export interface UpdateMailSettings {
    host: string;
    port: string;
    username: string;
    password: string;
    protocol: string;
    auth: boolean;
    sslEnable: boolean;
}

export interface User {
    id: number;
    email: string;
    createdAt: Date;
}