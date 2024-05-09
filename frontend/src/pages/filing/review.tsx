import type { TaxReturn, User } from "../../types";

import {
  Form,
  Grid,
  Alert,
  Label,
  Button,
  Fieldset,
  TextInput,
  ButtonGroup,
  GridContainer,
  StepIndicator,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAuth } from "../../contexts/auth-context";
import { Link, Navigate, useNavigate } from "react-router-dom";

export default function Review() {
  const navigate = useNavigate();

  const { t } = useTranslation();
  const { loading, jwt, user, setUser } = useAuth();

  const [error, setError] = useState<string | null>(null);

  const handleReviewInfo = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const res = await fetch(
      `http://localhost:8080/return/${user?.taxReturn?.id}/result?username=${user?.username}`,
      { method: "GET", headers: { Authorization: `Bearer ${jwt}` } }
    );

    if (res.ok) {
      const taxReturn: TaxReturn = await res.json();
      const updated = { ...user, taxReturn } as User;

      setUser(updated);
      navigate("/filing/results");
    } else {
      console.log("Error: ", res.status, res.statusText);
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
    return <h1>Loading ...</h1>;
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
          <StepIndicatorStep label={t("1099.title")} status="complete" />
          <StepIndicatorStep label={t("deductions.title")} status="complete" />
          <StepIndicatorStep label={t("review.title")} status="current" />
          <StepIndicatorStep label={t("results.title")} />
        </StepIndicator>

        {error && (
          <Alert type="error" headingLevel="h4" slim>
            {error}
          </Alert>
        )}

        <Form onSubmit={handleReviewInfo} className="w-full">
          <Fieldset legend={t("review.desc")}>
            {/* Personal */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/personal">{t("personal.title")}</Link>
              </h4>
              {/* Fname & Lname */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="first_name">{t("personal.fname")}</Label>
                  <TextInput
                    id="first_name"
                    name="first_name"
                    type="text"
                    defaultValue={user?.firstName ?? undefined}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="last_name">{t("personal.lname")}</Label>
                  <TextInput
                    id="last_name"
                    name="last_name"
                    type="text"
                    defaultValue={user?.lastName ?? undefined}
                    disabled
                  />
                </Grid>
              </Grid>

              {/* Birthdate & SSN */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="birthdate">{t("personal.birthdate")}</Label>
                  <TextInput
                    id="birthdate"
                    name="birthdate"
                    type="text"
                    defaultValue={user?.dateOfBirth ?? undefined}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="ssn">SSN or TIN</Label>
                  <TextInput
                    id="ssn"
                    name="ssn"
                    type="text"
                    defaultValue={user?.ssn ?? undefined}
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* Filing Information */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/filing-information">
                  {t("filing-info.title")}
                </Link>
              </h4>

              {/* Filing Status and Dependents */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="filing_status">
                    {t("filing-info.status")}
                  </Label>
                  <TextInput
                    id="filing_status"
                    name="filing_status"
                    type="text"
                    defaultValue={user?.taxReturn?.filingStatus ?? undefined}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="dependent">
                    {t("filing-info.isDependent")}
                  </Label>
                  <TextInput
                    id="dependent"
                    name="dependent"
                    type="text"
                    defaultValue={user?.taxReturn?.dependent ? "Yes" : "No"}
                    disabled
                  />
                </Grid>
              </Grid>

              {/* optional spouse fields based on filing statues */}
              {user?.taxReturn?.filingStatus === "Married, Filing Jointly" && (
                <Grid row gap>
                  <Grid tablet={{ col: true }}>
                    <Label htmlFor="spouseAgi">
                      {t("filing-info.spouse-income")}
                    </Label>
                    <TextInput
                      id="spouseAgi"
                      name="spouseAgi"
                      type="number"
                      defaultValue={user?.taxReturn?.spouseAgi ?? undefined}
                      disabled
                    />
                  </Grid>

                  <Grid tablet={{ col: true }}>
                    <Label htmlFor="spouseTaxWithheld">
                      {t("filing-info.spouse-withheld")}
                    </Label>
                    <TextInput
                      id="spouseTaxWithheld"
                      name="spouseTaxWithheld"
                      type="number"
                      defaultValue={
                        user?.taxReturn?.spouseTaxWithheld ?? undefined
                      }
                      disabled
                    />
                  </Grid>
                </Grid>
              )}
            </section>

            {/* W2 */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/w2">{t("w2.title")}</Link>
              </h4>

              {/* Employer Name, Wages, & Taxes Withheld */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="employer_name">{t("w2.ename")}</Label>
                  <TextInput
                    id="employer_name"
                    name="employer_name"
                    type="text"
                    defaultValue={user?.taxReturn?.formW2?.empName}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-wages" htmlFor="wages">
                    {t("review.wages")}
                  </Label>
                  <TextInput
                    id="wages"
                    name="wages"
                    type="number"
                    defaultValue={user?.taxReturn?.formW2?.income}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-w2_withheld" htmlFor="w2_withheld">
                    {t("w2.federal-withheld")}
                  </Label>
                  <TextInput
                    id="w2_withheld"
                    name="w2_withheld"
                    type="number"
                    defaultValue={user?.taxReturn?.formW2?.fedTaxWithheld}
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* Self Employment */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/self-employment">{t("1099.title")}</Link>
              </h4>

              {/* Non-Employment Compensation & Taxes Withheld */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="nec_wages">{t("1099.income")}</Label>
                  <TextInput
                    id="nec_wages"
                    name="nec_wages"
                    type="number"
                    defaultValue={user?.taxReturn?.form1099?.income}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-nec_withheld" htmlFor="nec_withheld">
                    {t("w2.federal-withheld")}
                  </Label>
                  <TextInput
                    id="nec_withheld"
                    name="nec_withheld"
                    type="number"
                    defaultValue={user?.taxReturn?.form1099?.fedTaxWithheld}
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* Deductions */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/deductions">{t("deductions.title")}</Link>
              </h4>
              {/* Mortgage Interest & Property Taxes Paid */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="claimedDependents">
                    {t("filing-info.dependents")}
                  </Label>
                  <TextInput
                    id="claimedDependents"
                    name="claimedDependents"
                    type="number"
                    defaultValue={
                      user?.taxReturn?.adjustment?.claimedDependents ?? 0
                    }
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="iraContribution">{t("deductions.ira")}</Label>
                  <TextInput
                    id="iraContribution"
                    name="iraContribution"
                    type="number"
                    defaultValue={
                      user?.taxReturn?.adjustment?.iraContribution ?? 0
                    }
                    disabled
                  />
                </Grid>
              </Grid>

              {/* Standard Deduction & Retirement Plan */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="stdDeduction">
                    {t("deductions.standard")}
                  </Label>
                  <TextInput
                    id="stdDeduction"
                    name="stdDeduction"
                    type="text"
                    defaultValue={
                      user?.taxReturn?.adjustment?.stdDeduction
                        ? "Standard Deduction"
                        : "Itemized Deduction"
                    }
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="retirementWorkPlan">
                    {t("deductions.retirement")}
                  </Label>
                  <TextInput
                    id="retirementWorkPlan"
                    name="retirementWorkPlan"
                    type="text"
                    defaultValue={
                      user?.taxReturn?.adjustment?.retirementWorkPlan
                        ? "Yes"
                        : "No"
                    }
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/deductions"
                  className="usa-button usa-button--outline"
                >
                  {t("back")}
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
