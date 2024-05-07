export interface FormW2 {
  id: number;
  eid: string;
  empName: string;
  empStreetAddress: string;
  empCity: string;
  empState: string;
  empZipCode: string;
  income: number;
  fedTaxWithheld: number;
  ssTaxWithheld: number;
  mediTaxWithheld: number;
}

export interface Form1099 {
  id: number;
  accountNum: string;
  income: number;
  fedTaxWithheld: number;
  payerName: string;
  payerState: string;
  payerZipCode: string;
}

export interface Adjustment {
  id: number;
  stdDeduction: true;
}

export interface TaxReturn {
  id: number;
  taxYear: string;
  filingStatus:
    | "Single"
    | "Married, Filing Separately"
    | "Married, Filing Jointly"
    | "Qualifying Surviving Spouse"
    | "Head of Household";
  dependent: boolean;
  spouseAgi: number | null;
  spouseTaxWithheld: number | null;
  adjGrossIncome: number | null;
  taxWithheld: number | null;
  taxableIncome: number | null;
  taxLiability: number | null;
  returnResult: number | null;
  formW2: FormW2 | null;
  form1099: Form1099 | null;
  adjustment: Adjustment | null;
}

export interface User {
  username: string;
  email: string | null;
  firstName: string | null;
  lastName: string | null;
  suffix: string | null;
  dateOfBirth: string | null;
  ssn: string | null;
  streetAddress: string | null;
  city: string | null;
  userState: string | null;
  zipCode: string | null;
  phoneNumber: string | null;
  taxReturn: TaxReturn | null;
}

export interface AuthResponse {
  username: string;
  password: string;
  jwt: string | null;
}
