import type { TaxReturn, User } from "../../types";

import {
  Form,
  Grid,
  Alert,
  Label,
  Radio,
  Button,
  Select,
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
import { Link, useNavigate, Navigate } from "react-router-dom";

export default function FilingInformation() {
  const navigate = useNavigate();

  const { t } = useTranslation();
  const { loading, jwt, user, updateUser } = useAuth();

  const [error, setError] = useState<string | null>(null);
  const [filingValue, setFilingValue] = useState(
    user?.taxReturn?.filingStatus ?? t("select")
  );

  const handleFilingInfo = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    // @ts-expect-error untyped form elements but we need the values
    const isDependent = e.currentTarget.elements.dependent.value === "Yes";

    const spouseAgi =
      filingValue === "Married, Filing Jointly"
        ? // @ts-expect-error untyped form elements but we need the values
          e.currentTarget.elements.spouseAgi.value
        : null;

    const spouseTaxWithheld =
      filingValue === "Married, Filing Jointly"
        ? // @ts-expect-error untyped form elements but we need the values
          e.currentTarget.elements.spouseTaxWithheld.value
        : null;

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      filingStatus: e.currentTarget.elements.filing_status.value,
      dependent: isDependent,
      spouseAgi,
      spouseTaxWithheld,
    };

    let res: Response;
    if (user?.taxReturn?.id) {
      // existing return so we should update the current one
      res = await fetch(
        `http://localhost:8080/return/${user.taxReturn.id}?username=${user.username}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${jwt}`,
          },
          body: JSON.stringify({ ...user.taxReturn, ...formData }),
        }
      );
    } else {
      res = await fetch(
        `http://localhost:8080/return?username=${user?.username}`,
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
      const data: TaxReturn = await res.json();

      // now we need to use this data to update the user object
      const updatedUser = {
        ...user,
        taxReturn: data,
      } as User;
      await updateUser(updatedUser);
      navigate("/filing/w2");
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
    return;
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
          <StepIndicatorStep label={t("filing-info.title")} status="current" />
          <StepIndicatorStep label={t("w2.title")} />
          <StepIndicatorStep label={t("1099.title")} />
          <StepIndicatorStep label={t("deductions.title")} />
          <StepIndicatorStep label={t("review.title")} />
          <StepIndicatorStep label={t("results.title")} />
        </StepIndicator>

        {error && (
          <Alert type="error" headingLevel="h4" slim>
            {error}
          </Alert>
        )}

        <Form onSubmit={handleFilingInfo} className="w-full">
          <Fieldset legend={t("filing-info.desc")}>
            <Grid row gap>
              <Grid tablet={{ col: 6 }}>
                <Label htmlFor="filing_status" requiredMarker>
                  {t("filing-info.status")}
                </Label>
                <Select
                  id="filing_status"
                  name="filing_status"
                  value={filingValue}
                  onChange={(e) => setFilingValue(e.target.value)}
                  required
                >
                  <option>{t("select")}</option>
                  <option value="Single">{t("filing-info.single")}</option>
                  <option value="Married, Filing Separately">
                    {t("filing-info.married-sep")}
                  </option>
                  <option value="Married, Filing Jointly">
                    {t("filing-info.married-jointly")}
                  </option>
                  <option value="Qualifying Surviving Spouse">
                    {t("filing-info.widow")}
                  </option>
                  <option value="Head of Household">
                    {t("filing-info.hoh")}
                  </option>
                </Select>
              </Grid>
            </Grid>

            {filingValue === "Married, Filing Jointly" && (
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="spouseAgi" requiredMarker>
                    {t("filing-info.spouse-income")}
                  </Label>
                  <TextInput
                    id="spouseAgi"
                    name="spouseAgi"
                    type="number"
                    defaultValue={user?.taxReturn?.spouseAgi ?? undefined}
                    required
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="spouseTaxWithheld" requiredMarker>
                    {t("filing-info.spouse-withheld")}
                  </Label>
                  <TextInput
                    id="spouseTaxWithheld"
                    name="spouseTaxWithheld"
                    type="number"
                    defaultValue={
                      user?.taxReturn?.spouseTaxWithheld ?? undefined
                    }
                    required
                  />
                </Grid>
              </Grid>
            )}

            <Grid row>
              <Grid tablet={{ col: true }}>
                <p>{t("filing-info.isDependent")}</p>
                <Radio
                  id="dependent-yes"
                  name="dependent"
                  value="Yes"
                  label={t("y")}
                  defaultChecked={user?.taxReturn?.dependent === true}
                />
                <Radio
                  id="dependent-no"
                  name="dependent"
                  value="No"
                  label={t("n")}
                  defaultChecked={user?.taxReturn?.dependent === false}
                />
              </Grid>
            </Grid>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/personal"
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
