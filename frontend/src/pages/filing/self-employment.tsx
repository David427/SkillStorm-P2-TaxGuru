import type { Form1099, TaxReturn } from "../../types";

import {
  Form,
  Grid,
  Alert,
  Label,
  Button,
  Select,
  Fieldset,
  TextInput,
  ButtonGroup,
  GridContainer,
  StepIndicator,
  TextInputMask,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { states } from "../../states";
import { FormEvent, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAuth } from "../../contexts/auth-context";
import { Link, Navigate, useNavigate } from "react-router-dom";

export default function SelfEmployment() {
  const navigate = useNavigate();

  const { t } = useTranslation();
  const { loading, jwt, user, updateReturn } = useAuth();

  const [error, setError] = useState<string | null>(null);

  const handleSelfEmploymentInfo = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      accountNum: e.currentTarget.elements.accountNum.value,
      // @ts-expect-error untyped form elements but we need the values
      payerName: e.currentTarget.elements.payerName.value,
      // @ts-expect-error untyped form elements but we need the values
      payerState: e.currentTarget.elements.payerState.value,
      // @ts-expect-error untyped form elements but we need the values
      payerZipCode: e.currentTarget.elements.payerZipCode.value,
      // @ts-expect-error untyped form elements but we need the values
      income: e.currentTarget.elements.income.value,
      // @ts-expect-error untyped form elements but we need the values
      fedTaxWithheld: e.currentTarget.elements.fedTaxWithheld.value,
    };

    let res: Response;

    if (user?.taxReturn?.form1099?.id) {
      res = await fetch(
        `http://localhost:8080/1099/${user.taxReturn.form1099.id}?username=${user.username}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${jwt}`,
          },
          body: JSON.stringify(formData),
        }
      );
    } else {
      res = await fetch(
        `http://localhost:8080/1099?username=${user?.username}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${jwt}`,
          },
          body: JSON.stringify(formData),
        }
      );
    }

    if (res.ok) {
      const form1099: Form1099 = await res.json();
      const updatedReturn = { ...user?.taxReturn, form1099 } as TaxReturn;
      await updateReturn(updatedReturn);

      navigate("/filing/deductions");
    } else {
      console.log("Error", res.status, res.statusText);
      if (res.headers.get("content-type")?.includes("application/json")) {
        const data = await res.json();
        setError(data.error);
      } else if (res.headers.get("content-type")?.includes("text/plain")) {
        const text = await res.text();
        setError(text);
      }
    }
  };

  if (loading) {
    return <h1>Loading...</h1>;
  }

  if (!jwt && !loading) {
    return <Navigate to="/login" />;
  }

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label={t("personal.title")} status="complete" />
          <StepIndicatorStep label={t("filing-info.title")} status="complete" />
          <StepIndicatorStep label={t("w2.title")} status="complete" />
          <StepIndicatorStep label={t("1099.title")} status="current" />
          <StepIndicatorStep label={t("deductions.title")} />
          <StepIndicatorStep label={t("review.title")} />
          <StepIndicatorStep label={t("results.title")} />
        </StepIndicator>

        {error && (
          <Alert type="error" headingLevel="h4" slim>
            {error}
          </Alert>
        )}

        <Form onSubmit={handleSelfEmploymentInfo} className="w-full">
          <Fieldset legend={t("1099.desc")}>
            {/* Account Number and Payer Name */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="accountNum" requiredMarker>
                  {t("1099.accountNum")}
                </Label>
                <TextInputMask
                  id="accountNum"
                  name="accountNum"
                  type="text"
                  mask="__________"
                  pattern="^\d{10}"
                  defaultValue={user?.taxReturn?.form1099?.accountNum}
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="payerName" requiredMarker>
                  {t("1099.payerName")}
                </Label>
                <TextInput
                  id="payerName"
                  name="payerName"
                  type="text"
                  defaultValue={user?.taxReturn?.form1099?.payerName}
                  required
                />
              </Grid>
            </Grid>

            {/* Payer State & Zip Code */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="payerState" requiredMarker>
                  {t("1099.payerState")}
                </Label>
                <Select
                  id="payerState"
                  name="payerState"
                  defaultValue={user?.taxReturn?.form1099?.payerState}
                  required
                >
                  <option>{t("select")}</option>
                  {states.map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </Select>
              </Grid>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="payerZipCode" requiredMarker>
                  {t("1099.payerZipCode")}
                </Label>
                <TextInputMask
                  id="payerZipCode"
                  name="payerZipCode"
                  type="number"
                  mask="_____"
                  pattern="^\d{5}"
                  defaultValue={user?.taxReturn?.form1099?.payerZipCode}
                  required
                />
              </Grid>
            </Grid>
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="income" requiredMarker>
                  {t("1099.income")}
                </Label>
                <TextInput
                  id="income"
                  name="income"
                  type="number"
                  defaultValue={user?.taxReturn?.form1099?.income}
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="fedTaxWithheld" requiredMarker>
                  {t("w2.federal-withheld")}
                </Label>
                <TextInput
                  id="fedTaxWithheld"
                  name="fedTaxWithheld"
                  type="number"
                  defaultValue={user?.taxReturn?.form1099?.fedTaxWithheld}
                  required
                />
              </Grid>
            </Grid>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/w2"
                  className="usa-button usa-button--outline"
                >
                  {t("back")}
                </Link>
                <Link
                  to="/filing/deductions"
                  className="usa-button usa-button--base"
                >
                  {t("skip")}
                </Link>
                <Button type="submit">{t("continue")}</Button>
              </ButtonGroup>
            </div>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
