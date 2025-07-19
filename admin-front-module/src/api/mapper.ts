import * as DTO from "./dto";
import {client} from "./config";

export const mapToProduct = (data: any): DTO.Product => ({
    id: Number(data.id),
    name: String(data.name),
    description: String(data.description),
    price: Number(data.price),
    stockQuantity: Number(data.stockQuantity),
    categoryName: String(data.categoryName),
    createdAt: new Date(data.createdAt),
    updatedAt: new Date(data.updatedAt),
    imageId: Number(data.imageId),
    imageUrl: data.imageId ? `${client.defaults.baseURL}/images/${data.imageId}` : undefined
});

export const mapToOrderResponse = (data: any): DTO.GetOrdersResponse => ({
    orders: data.orders.map(mapToOrderBody)
});

export const mapToOrderItem = (data: any): DTO.OrderItem => ({
    product: mapToProduct(data.product),
    quantity: Number(data.quantity),
    price: Number(data.total)
});

export const mapToCheckOrderResponse = (data: any): DTO.CheckOrderResponse => ({
    items: data.items.map(mapToOrderItem),
    total: Number(data.totalPrice)
});

export const mapToOrderBody = (data: any): DTO.OrderBody => ({
    items: data.items.map(mapToOrderItem),
    email: String(data.email),
    phone: String(data.phone),
    address: String(data.address),
    datetime: new Date(data.createdAt),
    total: Number(data.total)
});

export const mapToLoginResponse = (data: any): DTO.LoginResponse => ({
    email: String(data.email),
    ok: Boolean(data.ok)
});

export const mapToAuthResponse = (data: any): DTO.AuthResponse => ({
    id: Number(data.id),
    email: String(data.email)
});

export const mapToUser = (data: any): DTO.User => ({
    id: Number(data.id),
    email: String(data.email),
    createdAt: new Date(data.createdAt)
});