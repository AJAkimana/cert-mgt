export interface Customer {
  id: string;
  name: string;
  email?: string | null;
  status: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCustomerRequest {
  name: string;
  email?: string | null;
}
